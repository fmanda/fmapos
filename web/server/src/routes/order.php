<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require_once '../src/models/ModelORder.php';
require_once '../src/classes/DB.php';

//debug

$app->get('/test', function ($request, $response) {
	echo "test";
});

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

$app->get('/orderof/{id}/{date1}/{date2}/{limit}/{page}/[{fieldname}/{keyword}]', function ($request, $response, $args) {
	try{
		$keyword = '';
		$fieldname = 'orderno';
		if (isset($args['keyword'])) $keyword = $args['keyword'];
		if (isset($args['fieldname'])) $fieldname = $args['fieldname'];

		$sql = "select a.*, b.name as unit_name, c.name as customer from orders a
			left join units b on a.unit_id=b.id
			left join customer c on a.customer_id=c.id";

		if ($fieldname == "customer"){
			$fieldname = "c.name";
		}else if ($fieldname == "unit_name"){
				$fieldname = "b.name";
		}else{
			$fieldname = "a.". $fieldname;
		}
		$sql = $sql ." where ".$fieldname." like '%" . $keyword ."%'";
		$sql = $sql . " and a.company_id = ". $args['id'];
		$sql = $sql . " and a.orderdate between '". $args['date1'] . "' and '" . $args['date2']. "'";

		$data = DB::paginateQuery($sql, $args['limit'], $args['page']);
		return json_encode($data);
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
	try{
		$obj = ModelOrder::retrieve($args['id']);
		return json_encode($obj);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
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
