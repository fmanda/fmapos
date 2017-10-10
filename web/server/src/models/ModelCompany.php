<?php
	require '../src/models/BaseModel.php';

	class ModelCompany extends BaseModel{
		public static function getFields(){
			return array(
				"uid", "name", "address", "contact_person", "phone", "email"
			);
		}

		//override
		public static function retrieve($id){
			$obj = parent::retrieve($id);
			if (isset($obj)) $obj->items = ModelUnits::retrieveList('company_id = '. $obj->id);
			return $obj;
		}

		//units hanya diinsert dari model unit
		public static function saveToDB($obj){
			$db = new DB();
			$db = $db->connect();
			$db->beginTransaction();
			try {
				// if (!static::isNewTransaction($obj)){
				// 	$sql = ModelUnits::generateSQLDelete("company_id=". $obj->id);
				// 	$db->prepare($sql)->execute();
				// }
				static::saveObjToDB($obj, $db);
				// foreach($obj->items as $item){
				// 	$item->company_id = $obj->id;
				// 	ModelUnits::saveObjToDB($item, $db);
				// }
				$db->commit();
				$db = null;
			} catch (Exception $e) {
				$db->rollback();
				throw $e;
			}
		}

		public static function deleteFromDB($id){
			$str = static::generateSQLDelete("id=". $id);
			$str = $str . ModelUnits::generateSQLDelete("company_id=". $id);
			DB::executeSQL($str);
		}

	}

	class ModelUnits extends BaseModel{
		public static function getFields(){
			return array(
				"uid", "company_id", "name", "address", "phone"
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
