<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require_once '../src/models/ModelReconcile.php';
require_once '../src/models/ModelCashTrans.php';
require_once '../src/classes/DB.php';

//debug

$app->get('/testreconcile', function ($request, $response) {
	echo "test";
});

$app->get('/reconcile', function ($request, $response) {
	try{
		$list = ModelReconcile::retrieveList();
		return json_encode($list);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});

$app->get('/reconcileof/{id}/{date1}/{date2}/{limit}/{page}/[{fieldname}/{keyword}]', function ($request, $response, $args) {
	try{
		$keyword = '';
		$fieldname = 'unit_name';
		if (isset($args['keyword'])) $keyword = $args['keyword'];
		if (isset($args['fieldname'])) $fieldname = $args['fieldname'];

		if ($fieldname == "unit_name"){
			$fieldname = "b.name";
		}else{
			$fieldname = "a.". $fieldname;
		}

		$sql = "select b.name as unit_name, a.* from reconcile a inner join units b on a.unit_id = b.id";
		$sql = $sql ." where a.company_id = ". $args['id'];
		$sql = $sql ." and ".$fieldname." like '%" . $keyword ."%'";
		$sql = $sql . " and Date(a.transdate) between '". $args['date1'] . "' and '" . $args['date2']. "'";

		$data = DB::paginateQuery($sql, $args['limit'], $args['page']);
		return json_encode($data);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});


$app->post('/reconcile', function ($request, $response) {
	$json = $request->getBody();
	$obj = json_decode($json);
	try{
		$str = ModelReconcile::saveToDB($obj);
		return json_encode($obj);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}

});

//retrieve
$app->get('/reconcile/{id}', function ($request, $response, $args) {
	try{
		$obj = ModelReconcile::retrieve($args['id']);
		return json_encode($obj);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});

//delete
$app->delete('/reconcile/{id}', function (Request $request, Response $response) {
	$id = $request->getAttribute('id');
	try{
		ModelReconcile::deleteFromDB($id);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});


$app->post('/reconcilefromclient', function ($request, $response) {
	$json = $request->getBody();
	$obj = json_decode($json);

	// $log_filename = "log";
    // if (!file_exists($log_filename))
    // {
    //     mkdir($log_filename, 0777, true);
    // }
    // $log_file_data = $log_filename.'/log_' . date('d-M-Y') . '.log';
    // file_put_contents($log_file_data, $json . "\n", FILE_APPEND);


	try{
		$str = ModelReconcile::saveToDB($obj);
		return json_encode($obj);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}

});


$app->get('/cashtransof/{id}/{date1}/{date2}/{limit}/{page}', function ($request, $response, $args) {
	try{
		$sql = "select * from cashtrans";

		$sql = $sql ." where company_id = ". $args['id'];
		$sql = $sql . " and Date(transdate) between '". $args['date1'] . "' and '" . $args['date2']. "'";

		$data = DB::paginateQuery($sql, $args['limit'], $args['page']);
		return json_encode($data);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});

$app->delete('/cashtrans/{id}', function (Request $request, Response $response) {
	$id = $request->getAttribute('id');
	try{
		ModelCashTrans::deleteFromDB($id);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});

$app->post('/cashtransfromclient', function ($request, $response) {
	$json = $request->getBody();
	$obj = json_decode($json);

	$log_filename = "log";
    if (!file_exists($log_filename))
    {
        mkdir($log_filename, 0777, true);
    }
    $log_file_data = $log_filename.'/log_' . date('d-M-Y') . '.log';
    file_put_contents($log_file_data, $json . "\n", FILE_APPEND);
	try{
		ModelCashTrans::prepareUpload($obj);
		$str = ModelCashTrans::saveToDB($obj);
		return json_encode($obj);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}

});
