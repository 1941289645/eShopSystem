<template>
  <div style="width: 1100px;margin-left: -50px">

    <div style="margin: 10px 0">
      <el-input suffix-icon="el-icon-search" style="width:200px" placeholder="请输入订单编号" v-model="name"></el-input>
      <el-button  class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button  type="warning" @click="reset">重置</el-button>

<!--      <el-popconfirm-->
<!--          class="ml-5"-->
<!--          confirm-button-text='确定'-->
<!--          cancel-button-text='我再想想'-->
<!--          icon="el-icon-info"-->
<!--          icon-color="red"-->
<!--          title="您确定批量删除这些订单吗？"-->
<!--          @confirm="delBatch"-->
<!--      >-->
<!--        <el-button type="danger" slot="reference">删除订单<i class="el-icon-remove-outline"></i></el-button>  &lt;!&ndash;slot="reference"使按钮显示&ndash;&gt;-->
<!--      </el-popconfirm>-->
    </div>

    <el-table :data="tableData" stripe size="medium" @selection-change="handleSelectionChange">
      <el-table-column type="selection" ></el-table-column>  <!--选择框-->
      <el-table-column prop="autoId" label="ID"></el-table-column>
      <el-table-column prop="orderId" label="订单编号" width="160px"></el-table-column>
      <el-table-column prop="money" label="总金额"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template v-slot="scope">
          <el-tag type="info" v-if="scope.row.status===0">已取消</el-tag>
          <el-tag type="warning" v-if="scope.row.status===1">待支付</el-tag>
          <el-tag type="primary" v-if="scope.row.status===2">待发货</el-tag>
          <el-tag type="primary" v-if="scope.row.status===3">待收货</el-tag>
          <el-tag type="danger" v-if="scope.row.status===4">待评价</el-tag>
          <el-tag type="success" v-if="scope.row.status===5">已完成</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="orderDate" label="创建时间" width="100px"></el-table-column>
      <el-table-column prop="payTime" label="付款时间"></el-table-column>
      <el-table-column prop="payno" label="付款编号"></el-table-column>
      <el-table-column label="详情" width="100px">
        <template v-slot="scope">
          <el-button @click="detail(scope.row.orderId)">查看详情</el-button>
        </template>
      </el-table-column>


      <el-table-column label="操作" width="200" align="center">
        <template v-slot="scope">
          <el-button type="success" @click="changeStatus(scope.row,2)" v-if="scope.row.status ===1">付款 </el-button>
          <el-button type="warning" @click="changeStatus(scope.row,4)" v-if="scope.row.status ===3">确认收货 </el-button>
          <el-popconfirm
              v-if="scope.row.status ===1"
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定取消吗？"
              @confirm="changeStatus(scope.row,0)"
          >
            <el-button type="danger" slot="reference">取消</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0;margin: 10px 0;background: white">
      <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5,10]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="订单详情" :visible.sync="dialogFormVisible" width="50%" :close-on-click-modal="false">
      <el-table :data="orderdetails" border stripe size="small" >
        <el-table-column prop="productName" label="商品名称"></el-table-column>
        <el-table-column prop="price" label="商品单价"></el-table-column>
        <el-table-column prop="amount" label="商品数量"></el-table-column>
        <el-table-column prop="image" label="图片">
          <template slot-scope="scope">
            <el-image style="width: 100px;height: 100px" :src="scope.row.image" :preview-src-list="[scope.row.image]"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="image" label="评价">
          <template v-slot="scope">
            <el-button @click="handleComment(scope.row)" type="primary" v-if="status ===4 || status ===5">评价</el-button>
          </template>
        </el-table-column>
    </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog title="评价内容" :visible.sync="dialogFormVisible1" width="30%" :close-on-click-modal="false">
      <el-form >
        <el-form-item >
          <el-input v-model="comment" type="textarea" rows="4"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="saveComment">保 存</el-button>
      </div>
    </el-dialog>

  </div>

</template>

<script>
export default {
  name: "Orders",
  data(){
    return {
      showCommentBtn:false,
      tableData: [],
      total:0,
      pageNum:1,
      pageSize:10,
      name:"",
      form:{},
      dialogFormVisible:false,
      dialogFormVisible1:false,
      multipleSelection: [],
      headerBg:'headerBg',
      categorys:[],
      user:localStorage.getItem("user")? JSON.parse(localStorage.getItem("user")):{},
      orderdetails:[],
      comment:'',
      orderdetail:{},
      status:0
    }
  },
  created() {
    this.load()
  },
  methods: {
    handleComment(row){
      this.orderdetail=JSON.parse(JSON.stringify(row))
      this.comment=this.orderdetail.comment
      this.dialogFormVisible1=true
    },
    saveComment(){
      this.orderdetail.comment=this.comment
      this.request.post("/orderdetails/savecomment",this.orderdetail).then(res=>{
        if(res){
          this.$message.success("评价成功")
          this.dialogFormVisible1=false
          this.load()
        }else {
          this.$message.error("评价失败")
        }
      })
    },
    changeStatus(row,status) {
      row.status=status
      this.request.post("/orders/status",row).then(res=>{
        if(res){
          this.$message.success("操作成功")
          this.load()
        }else {
          this.$message.error("保存失败")
        }
      })
    },
    detail(orderId){
      let order=this.tableData.find(v=>v.orderId ===orderId)
      this.orderdetails=order.orderdetails;
      this.status=order.status
      this.dialogFormVisible=true
    },
    handleAdd(){
      this.dialogFormVisible=true
      this.form={}
    },
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row)) //深拷贝，解决没点确定表格数据变化问题
      this.dialogFormVisible=true
    },
    del(id){
      this.request.delete("/orders/"+autoId).then(res=>{
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
      let ids=this.multipleSelection.map(v => v.autoId)  //[{},{},{}] => [1,2,3] 把一个对象数组转换成纯数组
      this.request.post("/orders/del/batch",ids).then(res=>{
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
    },
    load(){
      this.request.get("/orders/page",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          orderId:this.name
        }
      }).then(res=>{
        this.tableData=res.records
        this.total=res.total
      })
    },
    changeEnable(row){
      this.request.post("/orders/update",row).then(res=>{
        if(res.code==='200'){
          this.$message.success("操作成功")
        }
      })
    },
    save(){
      this.request.post("/orders",this.form).then(res=>{
        if(res){
          this.$message.success("保存成功")
          this.dialogFormVisible=false
          this.load()
        }else {
          this.$message.error("保存失败")
        }
      })
    },
    reset(){
      this.name=""
      this.load()
    },
    handleSizeChange(pageSize){
      console.log(pageSize)
      this.pageSize=pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      console.log(pageNum)
      this.pageNum=pageNum
      this.load()
    },
    handleImgUploadSuccess(res){
      this.form.image=res
    }
  }

}
</script>

<style>
.headerBg{
  background: #eee !important;
}
</style>