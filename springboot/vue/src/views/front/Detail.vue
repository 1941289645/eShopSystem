<template>
  <div>
    <div style="display: flex;margin: 10px 0">
      <div style="width: 40%;">
  <!--        这个地方会报错,要用三段式-->
        <el-image :src="product.image?product.image:''" style="width: 100%;height: 320px"></el-image>
      </div>
      <div style="margin-left: 10px;flex: 1">
        <el-card>
          <el-form label-width="80px">
            <el-form-item label="商品名称">{{product.name}}</el-form-item>
            <el-form-item label="商品类别">{{category}}</el-form-item>
            <el-form-item label="商品价格"><span style="color: red" >{{product.price}}/{{product.unit}} </span></el-form-item>
            <el-form-item label="商品库存"><span>{{product.nums}} </span></el-form-item>

            <div>
              <el-input-number :value="1" size="medium" style="width: 150px" v-model="buyNum"></el-input-number>
              <el-button style="background: red;font-size: 16px;color: white;padding: 10px;margin-left: 5px;" @click="addCart">加入购物车</el-button>
            </div>

            <div style="margin-top: 20px;font-size: 12px;color: #888">
              温馨提示&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;·支持7天无理由退货
            </div>
          </el-form>
        </el-card>
      </div>
    </div>


    <div style="margin: 20px 0">
      <el-card>
        <div style="margin: 10px 0;font-size: 18px;border-bottom: 1px solid #ccc;padding-bottom: 10px">商品评价</div>
        <div v-for="item in comments" :key="item.id" style="margin: 10px 0">
          <div>
            <el-image style="width: 30px;height: 30px;border-radius: 50%" :src="item.avatar"></el-image>
            <span style="margin-left: 5px">{{item.member}}</span>
            <span style="margin-left: 20px;color: #666;font-size: 14px">{{item.comment}}</span>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "Detail",
  data(){
    let productId=this.$route.query.id
    return{
      product:{},
      productId:productId,
      category:"",
      buyNum:1,
      user:localStorage.getItem("user")? JSON.parse(localStorage.getItem("user")):{},
      comments:[]
    }
  },
  async created() {
    await this.request.get("/products/" + this.productId).then(res => {
      this.product = res
    })

    this.request.get("/productcategories/" + this.product.productCategoryId).then(res => {
      this.category = res.name
    })

    this.request.get("/orderdetails/comment/" +  this.productId).then(res => {
      this.comments = res.data
    })
  },
  methods:{
    addCart(){
      this.request.post("/cart",{
        productId:this.productId,
        num:this.buyNum,
        memberId:this.user.id
      }).then(res=>{
        if(res.code==='200'){
          this.$message.success('加入购物车成功')
        }else{
          this.$message.error(res.msg)
        }
      })
    },
  }
}
</script>

<style scoped>

</style>