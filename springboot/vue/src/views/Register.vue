p0<template>
  <div class="wrapper">
    <div style="margin: 100px auto; background-color: #fff; width: 350px;height: 460px;padding:20px;border-radius: 10px">
      <div style="margin: 15px 0;text-align: center;font-size: 24px"><b>注 册</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input placeholder="请输入账号" size="medium" style="margin: 5px 0" prefix-icon="el-icon-user" v-model="user.login"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入密码" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="请确认密码" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password v-model="user.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item prop="name">
          <el-input placeholder="请输入用户名" size="medium" style="margin: 5px 0" prefix-icon="el-icon-user"  v-model="user.name"></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input placeholder="请输入邮箱" size="medium" style="margin: 5px 0" prefix-icon="el-icon-message"  v-model="user.email"></el-input>
        </el-form-item>
        <el-form-item style="margin: 10px 0;text-align: right">
          <el-button type="primary" size="small" autocomplete="off" @click="register">注册</el-button>
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/login')">返回登录</el-button>
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
      user:{},
      rules: {
        login: [
          { required: true, message: '请输入账号', trigger: 'blur'},
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur'},
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        confirmPassword: [
          { required: true, message: '请输入密码', trigger: 'blur'},
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur'},
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur'},
          { pattern: /^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,20})*$/, message: '请输入正确的邮箱地址', trigger: 'blur'}
        ],
      }
    }
  },
  methods:{
    register(){
      this.$refs["userForm"].validate((valid) => {
        if (valid) {  //表单校验合法
          if(this.user.password!==this.user.confirmPassword){
            this.$message.error("两次输入密码不一致")
            return false
          }
          this.request.post("/members/register",this.user).then(res=>{
            if(res.code==='200'){
              this.$message.success("注册成功")
              this.$router.push('/login')
            }else {
              this.$message.error(res.msg)
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
    background-image: linear-gradient( 135deg, #FFA8A8 10%, #FCFF00 100%);
    overflow: hidden;
  }
</style>