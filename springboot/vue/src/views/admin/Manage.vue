<template>
    <el-container style="min-height: 100vh;">
      <el-aside :width="sideWidth+'px'" style="background-color: rgb(238, 241, 246);box-shadow: 2px 0 6px rgb(0 21 41 / 35%);" >
        <Aside :isCollapse="isCollapse" />  <!-- 组件引用，传参-->
      </el-aside>
      
      <el-container>
        <el-header style=" border-bottom: 1px solid #ccc">
          <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse" :user="user"/>
        </el-header>
        

        <el-main>
          <!-- 表示当前页面子路由会在router-view里面展示-->
          <router-view @refreshUser="getUser"/>
        </el-main>

      </el-container>
    </el-container>
</template>

<script>
import Aside from "@/components/Aside.vue";
import Header from "@/components/Header.vue";

export default {
  name: 'Home',
  data() {
      return{
        collapseBtnClass:'el-icon-s-fold',
        isCollapse:false,
        sideWidth:200,
        user:{}
      }
  },
  components: {
      Aside,
      Header
    },
  created() {
    this.getUser()
  },
  methods:{  //收缩按钮触发
    collapse(){  //收缩
       this.isCollapse=!this.isCollapse
       if(this.isCollapse){
        this.sideWidth=64
        this.collapseBtnClass="el-icon-s-unfold"
       }else{  //展开
        this.sideWidth=200
        this.collapseBtnClass="el-icon-s-fold"
       }
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

