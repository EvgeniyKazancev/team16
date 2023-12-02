#!/usr/bin/env python3
# coding: utf-8

# In[50]:


import warnings
warnings.filterwarnings("ignore")
import pandas as pd
import re
import nltk
nltk.download("stopwords")
nltk.download("punkt")
from nltk.corpus import stopwords
from string import punctuation

from pymorphy2 import MorphAnalyzer

from nltk.tokenize import word_tokenize


from pymystem3 import Mystem

import joblib
import pymysql
import os


# In[51]:


#обращаюсь к базе данных и получаю датасет c текстом новостей

def get_data():
    
    query = '''


SELECT *

FROM publications_text

'''
    
    conn = pymysql.connect(host='db.newsishorosho.ru', port=3306, user='mh16', passwd='exPwpf6SSb', db='test', charset='utf8')
    dict_cur = conn.cursor()
    dict_cur.execute(query)
    rows = dict_cur.fetchall()
    data = []
    for row in rows:
        data.append(row)
    return data

data = pd.DataFrame(get_data())


#посмотрим как выглядят данные в таблице:
#data.info()
#data.head()


# In[52]:


#обрабатываю датасет, объединяю новости в одну ячейку по id
data.columns = ['id','index','text','category_id']
data.reset_index(drop= True , inplace= True )

data_common = data.groupby(['id'])['text'].agg(lambda x: ' '.join(x.astype(str)))

data_common = pd.DataFrame({'id':data_common.index, 'text':data_common.values})
#data_common.info()
#data_common.head()


# In[53]:



#функции токенизации текста
additional_stopwords = ['которых','которые','твой','которой','которого','сих',
                        'ком','свой','твоя','этими','слишком','нами','всему',
                        'будь','саму','чаще','ваше','сами','наш','затем',
                        'самих','наши','ту','каждое','мочь','весь','этим',
                        'наша','своих','оба','который','зато','те','этих','вся',
                        'ваш','такая','теми','ею','которая','нередко','каждая',
                        'также','чему','собой','самими','нем','вами','ими',
                        'откуда','такие','тому','та','очень','сама','нему',
                        'алло','оно','этому','кому','тобой','таки','твоё',
                        'каждые','твои','нею','самим','ваши','ваша','кем','мои',
                        'однако','сразу','свое','ними','всё','неё','тех','хотя',
                        'всем','тобою','тебе','одной','другие','само','эта',
                        'самой','моё','своей','такое','всею','будут','своего',
                        'кого','свои','мог','нам','особенно','её','самому',
                        'наше','кроме','вообще','вон','мною','никто','это', 'в',
                        'о','по', 'об', 'его', 'из', 'а', 'со', 'что', 'который'
                        ]

stopwords_ru = stopwords.words('russian') + additional_stopwords
m = Mystem()

del_n = re.compile('\n')                # перенос каретки
del_tags = re.compile('<[^>]*>')        # html-теги
del_brackets = re.compile('\([^)]*\)')  # содержимое круглых скобок
clean_text = re.compile('[^а-яa-z\s]')  # все небуквенные символы кроме пробелов
del_spaces = re.compile('\s{2,}')

def prepare_text(text):
    text = del_n.sub(' ', str(text).lower())
    text = del_tags.sub('', text)
    text = del_brackets.sub('', text)
    res_text = clean_text.sub('', text)
    return del_spaces.sub(' ',res_text)

def del_stopwords(text):
    clean_tokens = tuple(
        map( lambda x: x if x not in stopwords_ru else '', word_tokenize(text) )
    )
    res_text = ' '.join(clean_tokens)
    return res_text

def lemmatize(text):
    lemmatized_text = ''.join(m.lemmatize(text))
    return lemmatized_text.split('|')


# In[54]:


#загружаю предобученную модель
#обрати внимание на путь к файлу  /opt/news-server/text_clf.joblib

file_path = r"/opt/news-server/text_clf.joblib"

loaded_model = joblib.load(file_path)


# In[55]:


#предобрабатываю текст и запускаю модель

data_common['text_clean'] = data_common['text'].apply(prepare_text)
all_texts = '|'.join(data_common['text_clean'].tolist())
clean_texts = del_stopwords(all_texts)
data_common['text_clean'] = lemmatize(clean_texts)
prediction = loaded_model.predict(data_common['text'])
data_common['prediction']=prediction

#data_common.tail(50)


# In[56]:


data_common.groupby('prediction').count()


# In[81]:


data_common['prediction'] = data_common['prediction'].astype(int)
data_common[data_common['prediction'] == 1.0]
data_common.to_csv('news_parser.csv')


# In[58]:


#обращаюсь к базе данных и получаю датасет c заголовками публикаций

def get_data():
    
    query = '''


SELECT *

FROM publications

'''
    
    conn = pymysql.connect(host='db.newsishorosho.ru', port=3306, user='mh16', passwd='exPwpf6SSb', db='test', charset='utf8')
    dict_cur = conn.cursor()
    dict_cur.execute(query)
    rows = dict_cur.fetchall()
    data = []
    for row in rows:
        data.append(row)
    return data

publications = pd.DataFrame(get_data())


# In[59]:


publications.columns = ['id', 'source_id', 'url', 'copies_count', 'created', 'removed']

publications_merged = publications.merge(data_common, on='id', how='left')
publications_merged['removed'] = publications_merged['prediction'].fillna(0)
publications_merged.drop(['text','text_clean','prediction'], axis = 1, inplace = True)
publications_merged.head()


# In[60]:



publications_merged['removed'] = publications_merged['removed'].astype(int)
publications_merged.info()


# In[89]:


#записываю результаты в бд

val = list(zip(data_common['prediction'],data_common['id']))


conn = pymysql.connect(host='db.newsishorosho.ru', port=3306, user='mh16', passwd='exPwpf6SSb', db='test', charset='utf8')
cursor = conn.cursor()

for value in val:
	(flag_removed, publication_id) = value
	try:
		cursor.execute("UPDATE publications SET removed = {} WHERE id = {}".format(flag_removed, publication_id))
	except:
		print('An exception occurred during update.')
  
conn.commit()


