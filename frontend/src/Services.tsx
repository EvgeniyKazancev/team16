import axios from 'axios';
import { baseUrl } from './constants';
import { Sources, NewsRequestProps } from './components/types/types';

// https://www.newsishorosho.ru/backend/login/auth?email=e.gorkavyi@gmail.com&password=qwerty

export const authRequest = async (email: string, password: string) => {
    const result = await axios.post(`${baseUrl}login/auth?email=${email}&password=${password}`).catch((err) => console.log(err));

    return result ? { status: result.status, id: result.data.message } : {};
};

export const codeRequest = async (uuid: string, code: string) => {
    const result = await axios.post(`${baseUrl}login/2auth?uuid=${uuid}&confirm2auth=${code}`).catch((err) => console.log(err));
    let resultObject;
    result
        ? (resultObject = {
              status: result.data.code,
              uuid: result.data.message
          })
        : {};
    return result ? resultObject : {};
};

// catIDs[] array[integer], sourceIDs[] array[integer], searchString string, favoritesOnly boolean.

export const newsRequest = async (props: NewsRequestProps) => {
    const sourceIdsString = props.sourceId
        ? props.sourceId
              .map((item) => {
                  return `&sourceIDs%5B%5D=${item}`;
              })
              .join('')
        : '';
    let resultErr;
    const result = await axios
        .get(
            `${baseUrl}pubs/get?uuid=${props.uuid}&startDate=${props.startDate}&endDate=${props.endDate}&page=${props.page}&size=${props.size}&sort=created,desc${
                props.catId ? '&catId=' + props.catId : ''
            }${sourceIdsString}${props.searchString ? '&searchString=' + props.searchString : ''}${props.favoritesOnly ? '&favoritesOnly=' + props.favoritesOnly : ''}`
        )
        .catch((err) => {
            console.log(err);
            resultErr = err;
        });
    // result ? console.log(result) : {};

    return result ? (result.status === 200 ? result.data : resultErr) : {};
};

export const sourcesRequest = async (uuid: string, questionId: string) => {
    const result = await axios.get(`${baseUrl}sources/getAll?uuid=${uuid}`);
    let resultObject;
    result
        ? (resultObject = {
              type: 'check',
              text: 'Список источников',
              id: '4',
              options: (resultObject = result.data.map((item: Sources) => {
                  return {
                      id: questionId + '-' + item.id,
                      text: item.name,
                      value: item.id
                  };
              }))
          })
        : {};

    return result ? resultObject : {};
};
