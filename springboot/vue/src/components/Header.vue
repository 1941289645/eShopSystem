<template>
  <div style="line-height: 60px;display: flex;">
    <div style="flex:1; ">
      <span :class="collapseBtnClass" style="cursor: pointer;font-size: 18px;" @click="collapse"></span>

      <el-breadcrumb separator="/" style="display: inline-block;margin-left: 10px">
        <el-breadcrumb-item :to="'/manage/home'" >首页</el-breadcrumb-item>
        <el-breadcrumb-item >{{ currentPathName }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <el-dropdown style="width: 150px; cursor: pointer; text-align: right">
      <div style="display: inline-block">
        <img :src="user.avatarurl" alt="" style="width: 30px;border-radius: 50%;position: relative;top:10px;right:5px">
        <span>{{ user.name }}</span>
        <i class="el-icon-arrow-down" style="margin-left: 5px;"></i>
      </div>
      <el-dropdown-menu slot="dropdown" style="width: 100px;text-align: center">
        <el-dropdown-item style="font-size: 14px;padding: 5px 0">
          <router-link to="/manage/person" style="text-decoration: none">个人信息</router-link>
        </el-dropdown-item>
        <span style="text-decoration: none" @click="logout">
          <el-dropdown-item style="font-size: 14px;padding: 5px 0">退出</el-dropdown-item>
        </span>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    collapse: '',
    user:Object
  },
  computed:{
    currentPathName(){
      return this.$store.state.currentPathName;      //需要监听的数据
    }
  },
  data(){
    return{
      // user:localStorage.getItem("user")? JSON.parse(localStorage.getItem("user")):{}
    }
  },
  watch:{
    currentPathName(newVal,oldVal){
      // console.log(newVal)
    }
  },
  methods:{
    logout(){
      this.$router.push("/login")
      localStorage.removeItem("user")
      this.$message.success("退出成功")
    }
  }
}
</script>

<style scoped>

</style>