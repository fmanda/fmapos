<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';
require_once '../src/classes/DB.php';

$app = new \Slim\App;

require_once 'variables.php';

// $app->options('/{routes:.+}', function ($request, $response, $args) {
//     return $response;
// });

$app->add(function ($req, $res, $next) {
    $response = $next($req, $res);
    return $response
            ->withHeader('Access-Control-Allow-Origin', '*')
            ->withHeader('Access-Control-Allow-Headers', 'X-Requested-With, Content-Type, Accept, Origin, Authorization')
            ->withHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
});

$app->get('/', function (Request $request, Response $response) {
    echo "server ready at " . $request->getUri();
});

$app->get('/hello/{name}', function (Request $request, Response $response) {
    $name = $request->getAttribute('name');
    $response->getBody()->write("Hello, $name");

    return $response;
});

require '../src/routes/company.php';
require '../src/routes/customer.php';
require '../src/routes/user.php';
require '../src/routes/product.php';
require '../src/routes/upload.php';
require '../src/routes/order.php';
require '../src/routes/ordercategory.php';

$app->run();
