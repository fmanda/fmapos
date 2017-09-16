<?php
	require '../src/models/BaseModel.php';

	class ModelTest extends BaseModel{
		public static function getFields(){
			return array("name");
		}

		//override
		public static function retrieve($id){
			$obj = parent::retrieve($id);
			$obj->items = ModelTestItem::retrieveList('header_id = '. $obj->id);
			return $obj;
		}

		//override
		public static function saveToDB($obj){
			$db = new DB();
			$db = $db->connect();
			$db->beginTransaction();
			try {
				if (!static::isNewTransaction($obj)){
					$sql = ModelTestItem::generateSQLDelete("header_id=". $obj->id);
					$db->prepare($sql)->execute();
				}
				static::saveObjToDB($obj, $db);
				foreach($obj->items as $item){
					$item->header_id = $obj->id;
					ModelTestItem::saveObjToDB($item, $db);
				}
				$db->commit();
				$db = null;
			} catch (Exception $e) {
				$db->rollback();
				echo $e->getMessage();
			}
		}

		public static function deleteFromDB($id){
			$str = static::generateSQLDelete("id=". $id);
			$str = $str . ModelTestItem::generateSQLDelete("header_id=". $id);
			// echo $str;
			DB::executeSQL($str);
		}

	}

	class ModelTestItem extends BaseModel{
		public static function getFields(){
			return array("name","header_id");
		}
	}
