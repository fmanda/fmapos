<?php

$container = $app->getContainer();
$container['upload_folder'] = 'uploads';
$container['temp_folder'] 	= 'temp';
$container['upload_directory'] = __DIR__ . DIRECTORY_SEPARATOR . $container['upload_folder'];
$container['temp_directory'] = __DIR__ . DIRECTORY_SEPARATOR . $container['temp_folder'];
