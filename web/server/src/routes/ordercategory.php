<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require_once '../src/models/ModelOrderCategory.php';
require_once '../src/classes/DB.php';


$app->get('/ordercategoryof/{company_id}', function ($request, $response) {
	$company_id = $request->getAttribute('company_id');
	// $unit_id = $request->getAttribute('unit_id');
	try{
		$list = ModelOrderCategory::retrieveList(
			' company_id = ' . $company_id
			// . ' and unit_id = ' . $unit_id
		);
		return json_encode($list);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});


$app->post('/ordercategory', function ($request, $response) {
	$json = $request->getBody();
	$obj = json_decode($json);
	try{
		$str = ModelOrderCategory::saveToDB($obj);
		return json_encode($obj);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}

});

//retrieve
$app->get('/ordercategory/{id}', function ($request, $response, $args) {
	$test = ModelOrderCategory::retrieve($args['id']);
	return json_encode($test);
});

//delete
$app->delete('/ordercategory/{id}', function (Request $request, Response $response) {
	$id = $request->getAttribute('id');
	try{
		ModelOrderCategory::deleteFromDB($id);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});
