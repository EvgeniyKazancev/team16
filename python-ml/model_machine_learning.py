#!/usr/bin/env python3
# coding: utf-8

# In[1]:


import warnings
warnings.filterwarnings("ignore")
import pandas as pd
import re
import nltk
nltk.download("stopwords")
nltk.download("punkt")
from nltk.corpus import stopwords
from string import punctuation

from nltk.tokenize import word_tokenize


from pymystem3 import Mystem

from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.naive_bayes import MultinomialNB
from imblearn.pipeline import Pipeline


import pymysql
import joblib


# In[2]:


def get_data():
    
    query = '''


SELECT *

FROM data_for_learning

'''
    
    conn = pymysql.connect(host='db.newsishorosho.ru', port=3306, user='mh16', passwd='exPwpf6SSb', db='test', charset='utf8')
    dict_cur = conn.cursor()
    dict_cur.execute(query)
    rows = dict_cur.fetchall()
    data = []
    for row in rows:
        data.append(row)
    return data

news_df_category = pd.DataFrame(get_data())


# In[3]:



news_df_category.columns = ['id','text','tags','news_category']
news_df_category['news_category'].fillna(0, inplace=True)


# In[4]:


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


news_df_category['text_clean'] = news_df_category['text'].apply(prepare_text)
all_texts = '|'.join(news_df_category['text_clean'].tolist())
clean_texts = del_stopwords(all_texts)
news_df_category['text_clean'] = lemmatize(clean_texts)


# In[7]:


#модель предсказания категории

x = news_df_category['text_clean']
y = news_df_category['news_category']
X_train, X_test, y_train, y_test = train_test_split(x, y, test_size=0.2)

text_clf = Pipeline([('vect', CountVectorizer(ngram_range=(1,2))),
                     ('tfidf', TfidfTransformer()), 
                    ('clf', MultinomialNB())])

text_clf = text_clf.fit(X_train, y_train)
y_pred = text_clf.predict(X_test)

#точность модели
#print('Score:', text_clf.score(X_test, y_test))


# In[8]:


# Сохранить модель
joblib.dump(text_clf, 'text_clf.joblib')

