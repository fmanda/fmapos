<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../src/models/ModelTest.php';

$app->get('/test', function () {
	$list = ModelTest::retrieveList();
	echo json_encode($list);
});

$app->post('/test', function (Request $request, Response $response) {
	$json = $request->getBody();
	$obj = json_decode($json);
	$str = ModelTest::saveToDB($obj);
	return json_encode($obj);
});

$app->get('/test/{id}', function (Request $request, Response $response) {
	$id = $request->getAttribute('id');
	$test = ModelTest::retrieve($id);
	return json_encode($test);
});

$app->delete('/test/{id}', function (Request $request, Response $response) {
	$id = $request->getAttribute('id');
	try{
		ModelTest::deleteFromDB($id);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});

$app->get('/testcollection/', function (Request $request, Response $response) {
	$test = new ModelTest();
	$test->name = "header";

	$item = new ModelTestItem();
	$item->name = "item 1";
	$test->items[] = $item;

	$item = new ModelTestItem();
	$item->name = "item 2";
	$test->items[] = $item;

	echo json_encode($test);
});
