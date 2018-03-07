<?php
	require_once '../src/models/BaseModel.php';

	class ModelOrder extends BaseModel{
		public static function getTableName(){
			return 'orders';
		}

		public static function getFields(){
			return array(
				"uid", "company_id", "unit_id", "orderno", "orderdate", "amount",
				"tax", "payment", "cashamount", "cardamount", "changeamount",
				"customer_id", "customfield_1", "customfield_2", "customfield_3",
				"user_create", "device", "notes", "reconcile_id"
			);
		}

		//override
		public static function retrieve($id){
			$obj = parent::retrieve($id);
			if (isset($obj)) $obj->items = ModelOrderItem::retrieveList(
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


					$sql = ModelOrderItem::generateSQLDelete(
						'company_id = '. $obj->company_id .' and order_id = '. $obj->id);

					$db->prepare($sql)->execute();

					$sql = 'delete from ordermodifier where company_id = '. $obj->company_id .
						' and orderitem_id in (select id from orderitem where order_id = '. $obj->id .')';

					$db->prepare($sql)->execute();

				}


				$obj->tax = 0;
				$obj->amount = 0;
				if (!isset($obj->payment)) $obj->payment = 0;
				if (!isset($obj->cashpayment)) $obj->cashpayment = 0;
				if (!isset($obj->cardpayment)) $obj->cardpayment = 0;
				if (!isset($obj->change)) $obj->change = 0;

				foreach($obj->items as $item){
					$item->id = 0; //force insert;
					$item->company_id = $obj->company_id;
					$item->unit_id = $obj->unit_id;


					if (!isset($item->discount)) $item->discount = 0;
					if (!isset($item->tax)) $item->tax = 0;

					$subtotal = $item->price * $item->qty;
					$item->total = $subtotal - $item->discount + $item->tax;
					$obj->amount = $obj->amount + $item->total;
					$obj->tax = $obj->tax + $item->tax;
				}


				static::saveObjToDB($obj, $db);
				foreach($obj->items as $item){
					$item->order_id = $obj->id;
					ModelOrderItem::saveToDB($item, $db);
				}
				$db->commit();
				$db = null;

			} catch (Exception $e) {
				$db->rollback();
				throw $e;
			}
		}

		public static function prepareUpload($obj) {

			try{
				if (isset($obj->customer_uid)) {
					$id = ModelCustomer::getIDFromUID($obj->customer_uid);
					if ($id>0) $obj->customer_id = $id;
				}
				if (isset($obj->reconcile_uid)) {
					$id = ModelReconcile::getIDFromUID($obj->reconcile_uid);
					if ($id>0) $obj->reconcile_id = $id;
				}

				foreach($obj->items as $item){
					ModelOrderItem::prepareUpload($item);
				}

			} catch (Exception $e) {
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

		public static function prepareUpload($obj) {

			if (isset($obj->product_uid)){
				try{
					$id = ModelProduct::getIDFromUID($obj->product_uid);
					if ($id>0) $obj->product_id = $id;
				} catch (Exception $e) {
					throw $e;
				}
			}

			if (isset($obj->modifiers)){
				try{
					foreach($obj->modifiers as $item){
						ModelOrderModifier::prepareUpload($item);
					}
				} catch (Exception $e) {
					throw $e;
				}
			}

		}

		public static function saveToDB($obj, $db){

			try {
				//delete units
				if (!isset($obj->company_id))
					throw new Exception("undeclared property company_id on object $classname", 1);
				if (!isset($obj->unit_id))
					throw new Exception("undeclared property unit_id on object $classname", 1);


				foreach($obj->modifiers as $item){
					$item->id = 0; //force insert;
					$item->company_id = $obj->company_id;
					$item->unit_id = $obj->unit_id;

				}
				// $sql = static::generateSQL($obj);
				// throw new Exception($sql, 1);

				static::saveObjToDB($obj, $db);

				foreach($obj->modifiers as $item){
					$item->orderitem_id = $obj->id;
					ModelOrderModifier::saveObjToDB($item, $db);
				}

			} catch (Exception $e) {
				// $db->rollback();
				throw $e;
			}
		}
	}

	class ModelOrderModifier extends BaseModel{
		public static function getFields(){
			return array(
				"uid", "company_id", "unit_id", "orderitem_id", "modifier_id", "modifier",  "price"
			);
		}

		public static function prepareUpload($obj) {
			// throw new Exception("Test" . json_encode($obj), 1);

			if (isset($obj->modifier_uid)){
				try{
					$id = ModelModifier::getIDFromUID($obj->modifier_uid);
					if ($id>0) $obj->modifier_id = $id;
				} catch (Exception $e) {
					throw $e;
				}
			}
		}
	}
