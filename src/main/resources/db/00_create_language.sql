create table repository.iso_639_3
(
    id        char(3) primary key comment 'The three-letter 639-3 identifier',
    part2b    char(3)      null comment 'Equivalent 639-2 identifier of the bibliographic applications code set, if there is one',
    part2t    char(3)      null comment 'Equivalent 639-2 identifier of the terminology applications code set, if there is one',
    part1     char(2)      null comment 'Equivalent 639-1 identifier, if there is one',
    scope     char(1)      not null comment 'I(individual), M(macrolanguage), S(special)',
    type      char(1)      not null comment 'A(ancient), C(constructed), E(extinct), H(historical), L(living), S(special)',
    ref_name  varchar(150) not null comment 'Reference language name',
    `comment` varchar(150) null comment 'Comment relating to one or more of the columns',
    constraint uk_iso_639_2b unique index (part2b),
    constraint uk_iso_639_2t unique index (part2t),
    constraint uk_iso_639_1 unique index (part1)
) comment 'ISO 639-3 Code Set';

create table repository.iso_639_3_name
(

    id            char(3)     not null comment 'The three-letter 639-3 identifier',
    print_name    varchar(75) not null comment 'One of the names associated with this identifier',
    inverted_name varchar(75) not null comment 'The inverted form of this Print_Name form'
) comment 'Language Names Index';

create table repository.iso_639_3_macrolanguage
(
    m_id     char(3) not null comment 'The identifier for a macrolanguage',
    i_id     char(3) primary key comment 'The identifier for an individual language that is a member of the macrolanguage',
    i_status char(1) not null comment 'A (active) or R (retired) indicating the status of the individual code element'
) comment 'Macrolanguage Mappings';

create table repository.iso_639_3_retirement
(
    id         char(3) primary key comment 'The three-letter 639-3 identifier',
    ref_name   varchar(150) not null comment 'reference name of language',
    ret_reason char(1)      not null comment 'code for retirement: C (change), D (duplicate), N (non-existent), S (split), M (merge)',
    change_to  char(3)      null comment 'in the cases of C, D, and M, the identifier to which all instances of this Id should be changed',
    ret_remedy varchar(300) null comment 'The instructions for updating an instance of the retired (split) identifier',
    effective  date         not null comment 'The date the retirement became effective'
) comment 'Deprecated (Retired) Code Element Mappings';