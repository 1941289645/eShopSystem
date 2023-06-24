<template>
  <el-card style="width:500px;">
    <el-form label-width="80px"  size="small">
      <el-upload
          class="avatar-uploader"
          action="http://localhost:9090/file/avartar/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :data="this.form"
      >
        <img v-if="form.avatarurl" :src="form.avatarurl" class="avatar" />
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>



      <el-form-item label="姓名">
        <el-input v-model="form.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" autocomplete="off" />
      </el-form-item>
      <el-form-item label="账号">
        <el-input v-model="form.login" autocomplete="off" :disabled="true"/>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="form.password" autocomplete="off" />
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
    this.getUser().then(res=>{
      console.log(this.user)
      this.form=res
    })
  },
  methods:{
    async getUser(){
      return  (await this.request.get("/members/id/" + this.user.id)).data
    },
    save(){
      this.request.post("/members",this.form).then(res=>{
        if(res){
          this.$message.success("保存成功")
          //触发父级更新user的方法
          this.$emit("refreshUser")
          //保存之后，触发manage的父级，通过父级中的功能来实现更新以及右上角头像的更新

          //更新浏览器存储信息
          this.getUser().then(res =>{
            res.token  = JSON.parse(localStorage.getItem("user")).token
            localStorage.setItem("user",JSON.stringify(res))
          })
        }else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAvatarSuccess(res){
      this.form.avatarurl = res
      this.$message.success("保存成功")
    }
  }
}
</script>

<style scoped>
.avatar-uploader{
  text-align: center;
  padding-bottom:10px
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 158px;
  height: 158px;
  display: block;
}
</style>