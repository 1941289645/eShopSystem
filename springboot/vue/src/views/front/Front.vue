<template>
  <div style="background-color: GhostWhite;min-height: 100vh">
<!--    头部-->
    <div style="display: flex;height: 60px;line-height: 60px;border-bottom: 1px solid #eee;background-color: white">
      <div style="width: 350px;display: flex;padding-left: 70px">
        <div style="width: 60px">
          <img src="../../assets/logo1.jpg" alt="" style="width: 50px;position: relative;top:5px;right: 0;border-radius: 50%">
        </div>
        <div style="flex: 1">易购商城</div>
      </div>

      <div style="flex: 1">
        <el-menu :default-active="$route.path" class="el-menu-demo" mode="horizontal" router style="border: none;height: 59px">
          <el-menu-item index="/front/home">首页</el-menu-item>
          <el-menu-item index="/front/cart">我的购物车</el-menu-item>
          <el-menu-item index="/front/orders">我的订单</el-menu-item>
        </el-menu>
      </div>
      <div style="width: 200px">
        <div v-if="!user.name" style="text-align: right;padding-right:30px">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown style="width: 150px;cursor: pointer;text-align: right">
            <div style="display: inline-block">
              <img :src="user.avatarurl" alt="" style="width: 30px;border-radius: 50%;position: relative;top:10px;right: 5px">
              <span>{{user.name}}</span><i class="el-icon-arrow-down" style="margin-left: 5px;"></i>
            </div>
            <el-dropdown-menu slot="dropdown" style="width: 100px;text-align: center">
              <el-dropdown-item style="font-size: 14px;padding: 5px 0">
                <router-link to="/front/person" style="text-decoration: none">个人信息</router-link>
              </el-dropdown-item>
              <span style="text-decoration: none" @click="logout">
                <el-dropdown-item style="font-size: 14px;padding: 5px 0">退出</el-dropdown-item>
              </span>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>

    <div style="width: 1000px;margin: 0 auto;padding-bottom: 100px">
      <router-view @refreshUser="getUser"/>
    </div>
  </div>
</template>

<script>
export default {
  name: "Front",
  data(){
    return{
      user:localStorage.getItem("user")? JSON.parse(localStorage.getItem("user")):{}
    }
  },
  created() {
    this.getUser()
  },
  methods:{
    logout(){
      this.$router.push("/login")
      localStorage.removeItem("user")
      this.$message.success("退出成功")
    },
    getUser(){
      //从后台获取数据
      let id = localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")).id:""
      this.request.get("/members/id/" + id).then(res =>{
        console.log(res.data)
        this.user = res.data
      })
    }
  }
}
</script>

<style scoped>

</style>