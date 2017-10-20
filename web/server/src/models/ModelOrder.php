<?php
	require_once '../src/models/BaseModel.php';

	class ModelOrder extends BaseModel{
		public static function getFields(){
			return array(
				"uid", "company_id", "unit_id", "orderno", "orderdate", "amount",
				"tax", "payment", "cashamount", "cardamount", "change", "uploaded",
				"customer_id", "customfield_1", "customfield_2", "customfield_3",
				"user_create", "device", "notes"
			);
		}

		//override
		public static function retrieve($id){
			$obj = parent::retrieve($id);
			if (isset($obj)) $obj->modifiers = ModelOrderItem::retrieveList(
				'company_id = '. $obj->company_id .' and order_id = '. $obj->id);
			return $obj;
		}

		public static function deleteFromDB($id){
			try{
				$obj = parent::retrieve($id);
				$str = static::generateSQLDelete("id=". $id);
				$str = $str . ModelOrderItem::generateSQLDelete(
					'company_id = '. $obj->company_id .' and order_id = '. $obj->id);
				DB::executeSQL($str);
			} catch (Exception $e) {
				$db->rollback();
				throw $e;
			}
		}

		public static function saveToDB($obj){
			$db = new DB();
			$db = $db->connect();
			$db->beginTransaction();
			try {
				//delete units
				if (!static::isNewTransaction($obj)){
					$sql = ModelOrderItem::generateSQLDelete(
						'company_id = '. $obj->company_id .' and order_id = '. $obj->id);
					$db->prepare($sql)->execute();
				}
				static::saveObjToDB($obj, $db);

				foreach($obj->units as $item){
					$item->id = 0; //force insert;
					$item->company_id = $obj->company_id;
					$item->unit_id = $obj->unit_id;
					$item->order_id = $obj->id;
					ModelOrderItem::saveObjToDB($item, $db);
				}
				$db->commit();
				$db = null;
			} catch (Exception $e) {
				$db->rollback();
				throw $e;
			}
		}

	}


	class ModelOrderItem extends BaseModel{
		public static function getFields(){
			return array(
				"uid", "company_id", "unit_id", "order_id", "product_id", "qty",
				"price", "discount", "tax" , "total", "notes"
			);
		}
	}
