<?php
	require_once '../src/models/BaseModel.php';

	class ModelReconcile extends BaseModel{
		public static function getFields(){
			return array(
				"uid", "company_id", "unit_id", "transdate", "sales_amount", "void_amount", "cash_amount",
				"card_amount", "cash_in", "cash_out", "total_order", "counter", "notes", "user_name"
			);
		}

		public static function saveToDB($obj){
			$classname = get_called_class();
			$db = new DB();
			$db = $db->connect();
			$db->beginTransaction();
			try {
				//delete units
				if (!isset($obj->company_id))
					throw new Exception("undeclared property company_id on object $classname", 1);
				if (!isset($obj->unit_id))
					throw new Exception("undeclared property unit_id on object $classname", 1);

				if (!static::isNewTransaction($obj)){
					//id masih pakai id client, set pakai id server
					if (isset($obj->restclient)){
						if ($obj->restclient){
							static::setIDByUID($obj);
						}
					}
				}
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
