<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require_once '../src/classes/DB.php';

$app->post('/upload', function ($request, $response) {
	// $company_id = $request->getAttribute('company_id');
	try{
		$directory = $this->get('temp_directory');
		if (!file_exists($directory)) {
		    mkdir($directory, 0777, true);
		}
	    $uploadedFiles = $request->getUploadedFiles();
		$uploadedFile  = $uploadedFiles["file"];
		$extension = pathinfo($uploadedFile->getClientFilename(), PATHINFO_EXTENSION);

		$filename = DB::guid(). '.' . $extension; //
	    $uploadedFile->moveTo($directory . DIRECTORY_SEPARATOR . $filename);

		$obj = new stdClass();
		$obj->url = $request->getUri()->getScheme() .'://'. $request->getUri()->getHost()
			. DIRECTORY_SEPARATOR .$this->get('temp_folder'). DIRECTORY_SEPARATOR .$filename;
		$obj->filename = $filename;

		return json_encode($obj);
	}catch(Exception $e){
		$msg = $e->getMessage();
		return $response->withStatus(500)
			->withHeader('Content-Type', 'text/html')
			->write($msg);
	}

});

$app->get('/upload/{filename}', function ($request, $response) {
	$filename = $request->getAttribute('filename');
	$filename = $this->get('upload_directory'). DIRECTORY_SEPARATOR .$filename;
	$image = @file_get_contents($filename);
	$response->write($image);
	return $response->withHeader('Content-Type', FILEINFO_MIME_TYPE);
	// echo '<img src="'.$filename.'" />';
});


$app->get('/upload', function ($request, $response) {
	return $request->getUri()->getScheme() .'://'. $request->getUri()->getHost();
});
