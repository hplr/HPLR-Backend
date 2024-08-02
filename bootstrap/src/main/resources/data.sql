insert into army_type (name)
values ('Mechanicum');
insert into army_type (name)
values ('Questoris Knights');
insert into army_type (name)
values ('Ultramarines');

insert into deployment (name)
values ('Hammer and Anvil');

insert into mission (name)
values ('Dominion');

insert into player(created_at,
                   id,
                   score,
                   registration_time,
                   updated_at,
                   user_id,
                   email,
                   motto,
                   name,
                   nickname,
                   pw_hash)
values (null,
        1,
        1400,
        '2024-06-23 18:57:39.000000',
        null,
        '543063bb-0829-49a8-b441-dadae49f2b64',
        'jan@jan.pl',
        'Ye',
        'Jan',
        'Yan',
        '1234')