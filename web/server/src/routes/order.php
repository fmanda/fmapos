<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require_once '../src/models/ModelORder.php';
require_once '../src/classes/DB.php';

//debug
$app->get('/order', function ($request, $response) {
	try{
		$list = ModelOrder::retrieveList();
		return json_encode($list);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});


$app->get('/orderof/{id}', function ($request, $response) {
	$id = $request->getAttribute('id');
	try{
		$list = ModelOrder::retrieveList('company_id = ' . $id);
		return json_encode($list);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});


$app->post('/order', function ($request, $response) {
	$json = $request->getBody();
	$obj = json_decode($json);
	try{
		$str = ModelOrder::saveToDB($obj);
		return json_encode($obj);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}

});

//retrieve
$app->get('/order/{id}', function ($request, $response, $args) {
	$test = ModelOrder::retrieve($args['id']);
	return json_encode($test);
});

//delete
$app->delete('/order/{id}', function (Request $request, Response $response) {
	$id = $request->getAttribute('id');
	try{
		ModelOrder::deleteFromDB($id);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});
