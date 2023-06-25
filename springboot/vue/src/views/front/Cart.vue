<template>
  <div style="margin: 10px 0">

    <div style="margin: 10px 0">
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline"></i></el-button>  <!--slot="reference"使按钮显示-->
      </el-popconfirm>
    </div>

    <el-table :data="tableData" border stripe size="medium" @selection-change="handleSelectionChange">
      <el-table-column type="selection" ></el-table-column>  <!--选择框-->
      <el-table-column  label="ID"><template slot-scope="scope">{{ idArray[scope.$index] }}</template></el-table-column>
      <el-table-column prop="productName" label="商品名称"></el-table-column>
      <el-table-column prop="price" label="商品单价"></el-table-column>
      <el-table-column prop="num" label="商品数量"></el-table-column>
      <el-table-column prop="image" label="图片">
        <template slot-scope="scope">
          <el-image style="width: 100px;height: 100px" :src="scope.row.image" :preview-src-list="[scope.row.image]"></el-image>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 20px;background-color: white;margin: 10px 0;text-align: right;border-radius: 10px">
      <div style="color: red">总价 ￥{{totalPrice}}</div>
      <div style="margin: 10px 0">
        <el-button style="background-color: red;color: white;font-size: 18px;padding: 10px 20px" @click="openDialog">下单</el-button>
      </div>
    </div>


    <el-dialog title="收货信息" :visible.sync="dialogFormVisible" width="30%" :close-on-click-modal="false">
      <el-form label-width="80px"  size="medium">
        <el-form-item prop="contactName" label="收货人">
          <el-input v-model="form.contactName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="contactPhoneNo" label="手机号码">
          <el-input v-model="form.contactPhoneNo" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="contactAddress" label="收货地址">
          <el-input v-model="form.contactAddress" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="adaOrder">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Cart",
  data(){
    return {
      tableData: [],
      idArray:[],
      dialogFormVisible:false,
      multipleSelection: [],
      user:localStorage.getItem("user")? JSON.parse(localStorage.getItem("user")):{},
      totalPrice:0,
      form:{
        contactName:"",
        contactPhoneNo:"",
        contactAddress:""
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    openDialog(){
      this.dialogFormVisible=true
    },
    adaOrder(){
      if(!this.multipleSelection.length){
        this.$message.error("下单失败，未选择商品")
        return
      }else{
        this.request.post("/orders/addOrder",this.multipleSelection,{params:this.form}).then(res=>{
          if(res){
            this.$message.success("下单成功")
            this.dialogFormVisible=false
            this.load()
          }else {
            this.$message.error("下单失败")
          }
        })
      }
    },
    del(id){
      this.request.delete("/cart/"+id).then(res=>{
        if(res){
          this.$message.success("删除成功")
          this.dialogFormVisible=false
          this.load()
        }else {
          this.$message.error("删除失败")
        }
      })
    },
    delBatch(){
      let ids=this.multipleSelection.map(v => v.id)  //[{},{},{}] => [1,2,3] 把一个对象数组转换成纯数组
      this.request.post("/cart/del/batch",ids).then(res=>{
        if(res){
          this.$message.success("批量删除成功")
          this.dialogFormVisible=false
          this.load()
        }else {
          this.$message.error("批量删除失败")
        }
      })
    },
    handleSelectionChange(val){
      this.multipleSelection=val
      this.request.post("/cart/cal",val).then(res=>{
        this.totalPrice=res.data
      })
    },
    async load() {
      await this.request.get("/cart/personCart").then(res => {
        this.tableData = res.data.list
        console.log(this.user)
      })
      this.idArray = Array.from({length: this.tableData.length}, (_, index) => index + 1);
    },

  }
}
</script>

<style scoped>

</style>