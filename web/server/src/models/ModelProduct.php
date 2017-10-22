<?php
	require_once '../src/models/BaseModel.php';

	class ModelProduct extends BaseModel{
		public static function getFields(){
			return array(
				"uid", "company_id", "sku", "name", "uom", "price", "tax", "barcode", "category", "img"
			);
		}

		//override
		public static function retrieve($id){
			$obj = parent::retrieve($id);
			if (isset($obj)) $obj->units = ModelUnitProduct::retrieveList(
				'company_id = '. $obj->company_id .' and product_id = '. $obj->id);
			if (isset($obj)) $obj->modifiers = ModelModifier::retrieveList(
				'company_id = '. $obj->company_id .' and product_id = '. $obj->id);
			return $obj;
		}

		public static function saveToDB($obj){
			$db = new DB();
			$db = $db->connect();
			$db->beginTransaction();
			try {
				//delete units
				if (!static::isNewTransaction($obj)){
					$sql = ModelUnitProduct::generateSQLDelete(
						'company_id = '. $obj->company_id .' and product_id = '. $obj->id);
					$sql = $sql . ModelModifier::generateSQLDelete(
						'company_id = '. $obj->company_id .' and product_id = '. $obj->id);
					$db->prepare($sql)->execute();
				}
				static::saveObjToDB($obj, $db);

				foreach($obj->units as $item){
					$item->id = 0; //force insert;
					$item->company_id = $obj->company_id;
					$item->product_id = $obj->id;
					ModelUnitProduct::saveObjToDB($item, $db);
				}

				foreach($obj->modifiers as $item){
					$item->company_id = $obj->company_id;
					$item->product_id = $obj->id;
					ModelModifier::saveObjToDB($item, $db);
				}

				$db->commit();
				$db = null;
			} catch (Exception $e) {
				$db->rollback();
				throw $e;
			}
		}

		public static function deleteFromDB($id){
			try{
				$obj = parent::retrieve($id);
				$str = static::generateSQLDelete("id=". $id);
				$str = $str . ModelUnitProduct::generateSQLDelete(
					'company_id = '. $obj->company_id .' and product_id = '. $obj->id);
				$str = $str . ModelModifier::generateSQLDelete(
					'company_id = '. $obj->company_id .' and product_id = '. $obj->id);
				DB::executeSQL($str);
			} catch (Exception $e) {
				$db->rollback();
				throw $e;
			}
		}

	}

	class ModelUnitProduct extends BaseModel{
		public static function getFields(){
			return array(
				"uid", "company_id", "product_id", "unit_id"
			);
		}
	}

	class ModelModifier extends BaseModel{
		public static function getFields(){
			return array(
				"uid", "company_id", "product_id", "name", "price"
			);
		}
	}
