<template>
  <div class="wrapper">
    <div style="margin: 200px auto; background-color: #fff; width: 350px;height: 350px;padding:20px;border-radius: 10px">
      <div style="margin: 20px 0;text-align: center;font-size: 24px"><b>登 录</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input  size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.login"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input  size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="role"  style="text-align: center">
          <el-radio v-model="user.role" label="system">管理员</el-radio>
          <el-radio v-model="user.role" label="user" >用户</el-radio>
        </el-form-item>
        <el-form-item style="margin: 10px 0;text-align: right">
          <el-button type="primary" size="small" autocomplete="off" @click="loginbtn">登录</el-button>
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data(){
    return{
      user:{
        role:""
      },
      rules: {
        login: [
          { required: true, message: '请输入用户名', trigger: 'blur'},
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur'},
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      }
    }
  },
  methods:{
    loginbtn(){
      this.$refs["userForm"].validate((valid) => {
        if (valid) {  //表单校验合法
          this.request.post("/members/login",this.user).then(res=>{
            if(res.code==='200' && this.user.role==='system' && this.user.role===res.data.role){
              localStorage.setItem("user",JSON.stringify(res.data))  //存储用户信息到浏览器
              this.$router.push("/manage/home")
              this.$message.success("登录成功")
            }else if(res.code==='200' && this.user.role==='user' && this.user.role===res.data.role){
              this.$router.push("/")
              this.$message.success("登录成功")
            }else {
              this.$message.error("账号或密码错误")
            }
          })
        }
      });
    }
  }
}
</script>

<style >
  .wrapper{
    height: 100vh;
    /*background-image: linear-gradient(to bottom right,#FC466B,#3F5EF8);*/
    background-image: linear-gradient( 135deg, #FFF3B0 10%, #CA26FF 100%);
    overflow: hidden;
  }
</style>