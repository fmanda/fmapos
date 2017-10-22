<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require_once '../src/models/ModelProduct.php';
require_once '../src/classes/DB.php';

$app->get('/productcategoryof/{id}', function ($request, $response) {
	$id = $request->getAttribute('id');
	try{
		$sql = 'select distinct category from Product where company_id = ' . $id
			.' and category is not null and category <>"" ';
		$list = DB::openQuery($sql);
		return json_encode($list);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});

$app->get('/productuomof/{id}', function ($request, $response) {
	$id = $request->getAttribute('id');
	try{
		$sql = 'select distinct uom from Product where company_id = ' . $id
			.' and category is not null and category <>"" ';
		$list = DB::openQuery($sql);
		return json_encode($list);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});


$app->get('/productof/{id}', function ($request, $response) {
	$id = $request->getAttribute('id');
	try{
		$list = ModelProduct::retrieveList('company_id = ' . $id);
		return json_encode($list);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});

$app->get('/productof/{id}/{limit}/{page}/[{fieldname}/{keyword}]', function ($request, $response, $args) {
	try{
		$keyword = '';
		$fieldname = 'name';
		if (isset($args['keyword'])) $keyword = $args['keyword'];
		if (isset($args['fieldname'])) $fieldname = $args['fieldname'];
		$sql = "select * from Product where ".$fieldname." like '%" . $keyword ."%'"
			. " and company_id = ". $args['id'];
		$data = DB::paginateQuery($sql, $args['limit'], $args['page']);
		return json_encode($data);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});


$app->post('/product', function ($request, $response) {
	$json = $request->getBody();
	$obj = json_decode($json);
	try{
		$str = ModelProduct::saveToDB($obj);
		try{
			$filename = $obj->img;
			$temp = $this->get('temp_directory') . DIRECTORY_SEPARATOR . $filename ;
			$upload = $this->get('upload_directory');
			if (!file_exists($upload)) mkdir($directory, 0777, true);
			$upload = $upload. DIRECTORY_SEPARATOR . $filename;
			echo $temp . "\n" . $upload;
			if (file_exists($temp)) rename($temp, $upload);

		}catch(Exception $e){
			return $response->withStatus(500)
				->withHeader('Content-Type', 'text/html')
				->write($e->getMessage());
		}

		return json_encode($obj);

	}catch(Exception $e){
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($e->getMessage());
	}

});

//retrieve
$app->get('/product/{id}', function ($request, $response, $args) {
	$test = ModelProduct::retrieve($args['id']);
	return json_encode($test);
});

//delete
$app->delete('/product/{id}', function (Request $request, Response $response) {
	$id = $request->getAttribute('id');
	try{
		ModelProduct::deleteFromDB($id);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}
});
