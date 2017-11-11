<?php
	require_once '../src/models/BaseModel.php';
	require_once '../src/models/ModelCompany.php';

	class ModelUser extends BaseModel{
		public static function getTableName(){
			return 'users';
		}

		public static function getFields(){
			return array(
				"uid", "company_id", "user_name", "password", "email_address", "company_id", "unit_id"
			);
		}

		public static function saveToDB($obj){
			$db = new DB();
			$db = $db->connect();
			$db->beginTransaction();
			try {
				static::saveObjToDB($obj, $db);
				$db->commit();
				$db = null;
			} catch (Exception $e) {
				$db->rollback();
				throw $e;
			}
		}

		public static function deleteFromDB($id){
			$str = static::generateSQLDelete("id=". $id);
			DB::executeSQL($str);
		}

		public static function getUserLogin($user, $password){
			$obj = DB::openQuery(
				'select * from '. static::getTableName()
				.' where user_name = "'. $user.'"'
				.' and password = "'. $password.'"'
			);
			if (isset($obj[0])) {
				$user = $obj[0];
				$user->company =  ModelCompany::retrieve($user->company_id);
				return $user;
			}
		}
	}
