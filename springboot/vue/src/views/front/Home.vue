<template>
  <div>
    <div style="margin: 10px 0">
      <el-carousel height="450px" :interval="10000">
        <el-carousel-item v-for="item in lun" :key="item.id">
          <a :href="item.link" target="_blank"><img :src="item.image" alt="" style="width: 100%;height: 100%"></a>
        </el-carousel-item>
      </el-carousel>
    </div>

    <div style="margin: 10px 0">
      <div style="margin: 20px 0;font-size: 20px;color: orangered;border-bottom:1px solid orangered;padding-bottom: 10px">商品列表</div>
      <el-row :gutter="10">
        <el-col :span="6" v-for="item in products" :key="item.id" style="margin-bottom: 10px">
          <div style="background-color: white;padding: 10px;cursor: pointer" @click="$router.push('/front/detail?id='+item.id)">
            <img :src="item.image" alt="" style="width: 100%;height: 200px;overflow: hidden;border-radius: 10px">
            <div style="color: #666;padding: 10px;" class="text-line1">{{item.name}}</div>
            <div style="margin: 10px 0;color:red;font-weight: bold;font-size: 14px">￥{{item.price}} / {{item.unit}}</div>
          </div>
        </el-col>
      </el-row>

      <div style="padding: 10px 0;">
        <el-pagination
            background
            align="center"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[4,8,12]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Home",
  data(){
    return{
      lun:[],
      products:[],
      total:0,
      pageNum:1,
      pageSize:8,
    }
  },
  created() {
    this.load()
  },
  methods:{
    load(){
      //获取轮播图
      this.request.get("/lunbo").then(res=>{
        this.lun=res
      })

      //获取产品展示
      this.request.get("/products/page",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          name:this.name
        }
      }).then(res=>{
        this.products=res.records
        this.total=res.total
      })
    },
    handleSizeChange(pageSize){
      this.pageSize=pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      this.pageNum=pageNum
      this.load()
    },
  }
}
</script>

<style scoped>

.text-line1{
  white-space:nowrap;
  overflow:hidden;
  text-overflow:ellipsis;
}

</style>