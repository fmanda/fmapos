<?php
	require_once '../src/models/BaseModel.php';

	class ModelOrderCategory extends BaseModel{
		public static function getFields(){
			return array(
				"uid", "company_id", "unit_id", "name", "customfield_1",
				"customfield_2",  "customfield_3",
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
	}
