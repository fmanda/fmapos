<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require_once '../src/models/ModelCompany.php';
require_once '../src/classes/DB.php';

//all list
$app->get('/company', function ($request, $response) {
	try{
		$list = ModelCompany::retrieveList();
		return json_encode($list);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});

$app->get('/company/{limit}/{page}/[{fieldname}/{keyword}]', function ($request, $response, $args) {
	try{
		$keyword = '';
		$fieldname = 'name';
		if (isset($args['keyword'])) $keyword = $args['keyword'];
		if (isset($args['fieldname'])) $fieldname = $args['fieldname'];

		$sql = "select * from Company where ".$fieldname." like '%" . $keyword ."%'";

		$data = DB::paginateQuery($sql, $args['limit'], $args['page']);
		return json_encode($data);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});


//insert/edit
$app->post('/company', function ($request, $response) {
	$json = $request->getBody();
	$obj = json_decode($json);
	try{
		$str = ModelCompany::saveToDB($obj);
		return json_encode($obj);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}

});

//retrieve
$app->get('/company/{id}', function ($request, $response, $args) {
	$test = ModelCompany::retrieve($args['id']);
	return json_encode($test);
});

//delete
$app->delete('/company/{id}', function (Request $request, Response $response) {
	$id = $request->getAttribute('id');
	try{
		ModelCompany::deleteFromDB($id);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});


//units

$app->get('/unitsof/{id}', function ($request, $response) {
	$id = $request->getAttribute('id');
	try{
		$list = ModelUnits::retrieveList('company_id = ' . $id);
		return json_encode($list);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});


$app->post('/units', function ($request, $response) {
	$json = $request->getBody();
	$obj = json_decode($json);
	try{
		$str = ModelUnits::saveToDB($obj);
		return json_encode($obj);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}

});

//retrieve
$app->get('/units/{id}', function ($request, $response, $args) {
	$test = ModelUnits::retrieve($args['id']);
	return json_encode($test);
});

//delete
$app->delete('/units/{id}', function (Request $request, Response $response) {
	$id = $request->getAttribute('id');
	try{
		ModelUnits::deleteFromDB($id);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});
