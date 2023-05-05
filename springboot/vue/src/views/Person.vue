<template>
  <el-card style="width:500px;">
    <el-form label-width="80px"  size="small">
      <el-form-item label="工号">
        <el-input v-model="form.id" autocomplete="off" />
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="form.tname" autocomplete="off" />
      </el-form-item>
      <el-form-item label="性别">
        <el-input v-model="form.tsex" autocomplete="off" />
      </el-form-item>
      <el-form-item label="生日">
        <el-input v-model="form.tbirthday" autocomplete="off" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
export default {
  name: "Person",
  data(){
    return{
      form:{},
      user:localStorage.getItem("user")? JSON.parse(localStorage.getItem("user")):{}
    }
  },
  created() {
    this.request.get('/teacher/id/'+this.user.id).then(res=>{
      // if(res==='200'){
      //   this.form=res.data
      // }
      console.log(this.user)
      this.form=res.data
    })
  },
  methods:{
    save(){
      this.request.post("/teacher",this.form).then(res=>{
        if(res){
          this.$message.success("保存成功")
        }else {
          this.$message.error("保存失败")
        }
      })
    },
  }
}
</script>

<style scoped>

</style>