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
			return $obj[0];
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
			$sql = "";
			$strvalue = "";
			$fields = static::getFields();
			foreach ($fields as $field) {
				if ($sql<>""){
					$sql = $sql . ",";
					$strvalue = $strvalue . ",";
				}
			    $sql = $sql. $field;
				$strvalue = $strvalue. "'". $obj->{$field} ."'";
			}
			$sql = "insert into ". static::getTableName() . "(" . $sql .")";
			$sql = $sql. "values(" . $strvalue . ");";
			return $sql;
		}

		public static function generateSQLUpdate($obj){
			$strvalue = "";
			$fields = static::getFields();
			foreach ($fields as $field) {
				if ($strvalue<>""){
					$strvalue = $strvalue . ",";
				}
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
			try {
				$sql = static::generateSQL($obj);
				$int = $db->prepare($sql)->execute();
				if (static::isNewTransaction($obj)){
					$obj->id = $db->lastInsertId();
				}
			} catch (Exception $e) {
				$db->rollback();
				echo $e->getMessage();
			}
		}


	}
