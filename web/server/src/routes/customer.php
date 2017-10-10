<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require_once '../src/models/ModelCustomer.php';
require_once '../src/classes/DB.php';


$app->get('/customerof/{id}', function ($request, $response) {
	$id = $request->getAttribute('id');
	try{
		$list = ModelCustomer::retrieveList('company_id = ' . $id);
		return json_encode($list);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});

$app->get('/customerof/{id}/{limit}/{page}/[{fieldname}/{keyword}]', function ($request, $response, $args) {
	try{
		$keyword = '';
		$fieldname = 'name';
		if (isset($args['keyword'])) $keyword = $args['keyword'];
		if (isset($args['fieldname'])) $fieldname = $args['fieldname'];

		$sql = "select a.*, b.name as unit_name from customer a inner join units b on a.unit_id=b.id";
		if ($fieldname == "unit_name"){
			$fieldname = "b.name";
		}else{
			$fieldname = "a.". $fieldname;
		}

		$sql = $sql ." where ".$fieldname." like '%" . $keyword ."%'";
		$sql = $sql . " and a.company_id = ". $args['id'];
		$data = DB::paginateQuery($sql, $args['limit'], $args['page']);
		return json_encode($data);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});


$app->post('/customer', function ($request, $response) {
	$json = $request->getBody();
	$obj = json_decode($json);
	try{
		$str = ModelCustomer::saveToDB($obj);
		return json_encode($obj);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}

});

//retrieve
$app->get('/customer/{id}', function ($request, $response, $args) {
	$test = ModelCustomer::retrieve($args['id']);
	return json_encode($test);
});

//delete
$app->delete('/customer/{id}', function (Request $request, Response $response) {
	$id = $request->getAttribute('id');
	try{
		ModelCustomer::deleteFromDB($id);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});
