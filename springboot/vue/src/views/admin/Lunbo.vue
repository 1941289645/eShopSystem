<template>
  <div>


    <div style="margin: 10px 0">
      <el-input suffix-icon="el-icon-search" style="width:200px" placeholder="请输入名称" v-model="name"></el-input>
      <el-button  class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button  type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i></el-button>
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

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection" ></el-table-column>  <!--选择框-->
      <el-table-column prop="id" label="ID"></el-table-column>
      <el-table-column prop="image" label="图片">
        <template slot-scope="scope">
          <el-image style="" :src="scope.row.image" :preview-src-list="[scope.row.image]"></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="link" label="商品链接"></el-table-column>
      <el-table-column prop="productId" label="商品id"></el-table-column>
      <el-table-column prop="productName" label="商品名称"></el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
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
    <div style="padding: 10px 0;">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2,5,10,20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px"  size="small">
        <el-form-item prop="img" label="图片">
          <el-upload action="http://localhost:9090/file/lunbo/upload"
                     ref="image"
                     :on-success="handleImgUploadSuccess"
                     :limit="1"
          >
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="productId" label="商品">
          <el-select clearable v-model="form.productId" placeholder="请选择"  style="width: 100%"  @change="changeStatus(form.productId)">
            <el-option v-for="item in products" :key="item.id" :label ="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="link" label="商品链接">
          <el-input v-model="form.link" autocomplete="off" disabled></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
export default {
  name: "Products",
  data(){
    return {
      tableData: [],
      total:0,
      pageNum:1,
      pageSize:10,
      name:"",
      form:{},
      dialogFormVisible:false,
      multipleSelection: [],
      headerBg:'headerBg',
      products:[],
      user:localStorage.getItem("user")? JSON.parse(localStorage.getItem("user")):{}
    }
  },
  created() {
    this.load()
  },
  methods: {
    changeStatus(productId){
      let products=this.products.find(v => v.id===productId)
      this.form.link='/front/detail?id='+productId
      this.form.productName=products.name
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
      this.request.delete("/lunbo/"+id).then(res=>{
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
      this.request.post("/lunbo/del/batch",ids).then(res=>{
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
      console.log(val)
      this.multipleSelection=val
    },
    load(){
      this.request.get("/lunbo/page",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          name:this.name
        }
      }).then(res=>{
        // console.log(res)
        this.tableData=res.records
        this.total=res.total
      })
      this.request.get("/products").then(res=>{
        this.products=res
        console.log(this.products)
      })
    },
    changeEnable(row){
      this.request.post("/lunbo/update",row).then(res=>{
        if(res.code==='200'){
          this.$message.success("操作成功")
        }
      })
    },
    save(){
      this.request.post("/lunbo",this.form).then(res=>{
        if(res){
          this.$message.success("保存成功")
          this.dialogFormVisible=false
          this.load()
        }else {
          this.$message.error("保存失败")
        }
      })
      this.$refs.image.clearFiles();
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