export type NewsInfo = {
    id: string;
    title: string;
    datePublished: string;
    timePublsihed: string;
    source: string;
    description: string;
};

export type Options = {
    text: string;
    id: string;
    value: string | number;
};

export type Question = {
    type: string;
    text: string;
    id: string;
    options: Options[];
};

export type Content = {
    id: number;
    created: string;
    img: PublicationsData[];
    removed: boolean;
    source: string;
    url: string;
    text: PublicationsText[];
    usersWhoHaveFavorited: string[];
};

export type Info = {
    totalElements: number;
    totalPages: number;
    content: Content[];
};

export type PublicationsData = {
    content: string;
    id: number;
    property: string;
    publicationId: number;
};

export type PublicationsText = {
    header: boolean;
    id: number;
    publicationId: number;
    text: string;
};

export type Item = {
    categories: [];
    copiesCount: number;
    created: string;
    id: number;
    publicationsData: PublicationsData[];
    publicationsText: PublicationsText[];
    removed: boolean;
    source: {
        created: string;
        id: number;
        parseDepth: null;
        sourceType: string;
        url: string;
    };
    url: string;
    usersWhoHaveFavorited: [];
};

export type Sources = {
    id: number;
    url: string;
    name: string;
    sourceType: string;
    parseDepth: null | number;
    created: string;
};

export type filterInfo = {
    type: string;
    text: string;
    id: string;
    options: {
        value: string;
        id: string;
    }[];
};

export type NewsRequestProps = {
    uuid: string | null;
    startDate: string;
    endDate: string;
    page: number;
    size: number | string;
    catId?: (number | string)[];
    sourceId?: (number | string)[];
    searchString?: string;
    favoritesOnly?: boolean;
};
