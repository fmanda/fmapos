<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require_once '../src/models/ModelUser.php';
require_once '../src/classes/DB.php';


$app->post('/login', function ($request, $response) {
	$json = $request->getBody();

	$obj = json_decode($json);

	try{
		$obj = Modeluser::getUserLogin($obj->user_name, $obj->password);
		return json_encode($obj);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}

});
