<?php
	require_once '../src/classes/DB.php';

	class BaseModel{
		public static function getFields(){
			return array("id");
		}

		public static function getTableName(){
			$str = get_called_class();
			$str = str_replace("Model","",$str);
			return $str;
		}

		public static function retrieve($id){
			$obj = DB::openQuery('select * from '.static::getTableName().' where id='.$id);
			if (isset($obj[0])) return $obj[0];
		}

		public static function retrieveList($filter=''){
			$sql = 'select * from '.static::getTableName().' where 1=1 ';
			if ($filter<>''){
				$sql = $sql .' and '. $filter;
			}
			$obj = DB::openQuery($sql);
			return $obj;
		}

		public static function generateSQLInsert($obj){
			$classname = get_called_class();
			try{
				if ($obj == null){
					throw new Exception("[BaseModel] Object is null \n");
				}
				$sql = "";
				$strvalue = "";
				$fields = static::getFields();
				foreach ($fields as $field) {
					if ($field == "uid"){
						$obj->{$field} = DB::GUID();
					}
					if ($sql<>""){
						$sql = $sql . ",";
						$strvalue = $strvalue . ",";
					}
				    $sql = $sql. $field;
					if (!isset($obj->{$field}))
						throw new Exception("undeclared property $field on object $classname", 1);
					$strvalue = $strvalue. "'". $obj->{$field} ."'";
				}
				$sql = "insert into ". static::getTableName() . "(" . $sql .")";
				$sql = $sql. "values(" . $strvalue . ");";
				return $sql;
			}catch(Exception $e){
				throw $e;
			}
		}

		public static function generateSQLUpdate($obj){
			$strvalue = "";
			$fields = static::getFields();
			$classname = get_called_class();
			foreach ($fields as $field) {
				if ($field == "uid") continue;
				if ($strvalue<>""){
					$strvalue = $strvalue . ",";
				}
				if (!isset($obj->{$field}))
					throw new Exception("undeclared property $field on object $classname", 1);

				$strvalue = $strvalue. $field ." = '". $obj->{$field} ."'";
			}
			$sql = "update ". static::getTableName() . " set " . $strvalue;
			$sql = $sql. " where id = " . $obj->id . ";";
			return $sql;
		}

		public static function isNewTransaction($obj){
			$do_insert = !isset($obj->id);
			if (!$do_insert){
				$do_insert = ($obj->id <= 0);
			};
			return $do_insert;
		}

		public static function generateSQL($obj){
			if (static::isNewTransaction($obj)) {
				return static::generateSQLInsert($obj);
			}else{
				return static::generateSQLUpdate($obj);
			}
		}

		public static function generateSQLDelete($filter){
			return "delete from " . static::getTableName() . " where " . $filter .";";
		}

		public static function saveObjToDB($obj, $db){
			$sql = static::generateSQL($obj);
			try {
				$int = $db->prepare($sql)->execute();
				if (static::isNewTransaction($obj)){
					$obj->id = $db->lastInsertId();
				}
			} catch (Exception $e) {
				$db->rollback();
				throw $e;
			}
		}

		//master detail example :
		// public static function saveToDB($obj){
		// 	$db = new DB();
		// 	$db = $db->connect();
		// 	$db->beginTransaction();
		// 	try {
		// 		if (!static::isNewTransaction($obj)){
		// 			$sql = ModelUnits::generateSQLDelete("company_id=". $obj->id);
		// 			$db->prepare($sql)->execute();
		// 		}
		// 		static::saveObjToDB($obj, $db);
		// 		foreach($obj->items as $item){
		// 			$item->company_id = $obj->id;
		// 			ModelUnits::saveObjToDB($item, $db);
		// 		}
		// 		$db->commit();
		// 		$db = null;
		// 	} catch (Exception $e) {
		// 		$db->rollback();
		// 		throw $e;
		// 	}
		// }


	}
