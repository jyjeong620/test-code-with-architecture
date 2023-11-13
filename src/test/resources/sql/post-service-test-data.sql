
insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values (1, 'jeongjy620@gmail.com', 'jeongjy', 'seoul', 'aaaa-aaaaa-aaaaaa', 'ACTIVE', 0);
insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values (2, 'jeongjy630@gmail.com', 'jeongjy01', 'seoul', 'aaaa-aaaaa-aaaaaa2', 'PENDING', 0);

insert into `posts` (`id`, `content`, `created_at`, `modified_at`, `user_id`)
values (1, 'helloworld', 1678530673958, 0, 1);