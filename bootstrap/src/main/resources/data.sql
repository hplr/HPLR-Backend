delete from army_type;
ALTER SEQUENCE army_type_id_seq RESTART WITH 1;
insert into army_type (name)
values ('Mechanicum');
insert into army_type (name)
values ('Questoris Knights');
insert into army_type (name)
values ('Ultramarines');

delete from deployment;
ALTER SEQUENCE deployment_id_seq RESTART WITH 1;
insert into deployment (name)
values ('Hammer and Anvil');

delete from mission;
ALTER SEQUENCE mission_id_seq RESTART WITH 1;
insert into mission (name)
values ('Dominion');
