<?php

	class DB{
		private $dbhost = 'localhost';
		private $dbname = 'test';
		private $dbuser = 'root';
		private $dbpassword = '';

		public function connect(){
			$mysql_connect_str = "mysql:host=$this->dbhost;dbname=$this->dbname";
			$conn = $dbconnection = new PDO($mysql_connect_str, $this->dbuser, $this->dbpassword);
			$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
			return $conn;
		}

		public static function openQuery($sql){
			try{
				$db = new DB();
				$db = $db->connect();
				$stmt = $db->query($sql);
				$rows = $stmt->fetchAll(PDO::FETCH_OBJ);
				$db = null;
				return $rows;
			}catch(PDOException $e){
				echo '{"error":{"text": '. $e->getMessage() .'}}' ;
				throw $e;
			}
		}

		public static function executeSQL($sql){
			$db = new DB();
			$db = $db->connect();
			$db->beginTransaction();
			try {
				$int = $db->prepare($sql)->execute();
				$db->commit();
			} catch (Exception $e) {
				$db->rollback();
				throw $e;
			}
		}

	}
