<template>
  <div>
    <!-- 搜索区域 -->
    <div class="search">
      <!-- 输入框，用于输入要查询的公告标题 -->
      <el-input placeholder="请输入标题查询" style="width: 200px" v-model="title"></el-input>
      <!-- 查询按钮，点击时调用 load 方法 -->
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <!-- 重置按钮，点击时调用 reset 方法 -->
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <!-- 操作区域 -->
    <div class="operation">
      <!-- 新增按钮，点击时调用 handleAdd 方法 -->
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <!-- 批量删除按钮，点击时调用 delBatch 方法 -->
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <!-- 表格区域 -->
    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
        <!-- 表格选择列 -->
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <!-- 序号列 -->
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <!-- 标题列 -->
        <el-table-column prop="title" label="标题" show-overflow-tooltip></el-table-column>
        <!-- 内容列 -->
        <el-table-column prop="content" label="内容" show-overflow-tooltip></el-table-column>
        <!-- 创建时间列 -->
        <el-table-column prop="time" label="创建时间"></el-table-column>
        <!-- 创建人列 -->
        <el-table-column prop="user" label="创建人"></el-table-column>

        <!-- 操作列 -->
        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <!-- 编辑按钮 -->
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
            <!-- 删除按钮 -->
            <el-button plain type="danger" size="mini" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页控件 -->
      <div class="pagination">
        <el-pagination background @current-change="handleCurrentChange" :current-page="pageNum"
          :page-sizes="[5, 10, 20]" :page-size="pageSize" layout="total, prev, pager, next" :total="total">
        </el-pagination>
      </div>
    </div>

    <!-- 编辑/新增公告对话框 -->
    <el-dialog title="信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <!-- 标题输入框 -->
        <el-form-item prop="title" label="标题">
          <el-input v-model="form.title" autocomplete="off"></el-input>
        </el-form-item>
        <!-- 内容输入框 -->
        <el-form-item prop="content" label="内容">
          <el-input type="textarea" :rows="5" v-model="form.content" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <!-- 取消按钮 -->
        <el-button @click="fromVisible = false">取 消</el-button>
        <!-- 确定按钮 -->
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Notice", // 组件名称
  data() {
    return {
      tableData: [],  // 表格数据
      pageNum: 1,   // 当前页码
      pageSize: 10,  // 每页显示的记录数
      total: 0,  // 总记录数
      title: null,  // 查询用的公告标题
      fromVisible: false,  // 控制对话框的显示状态
      form: {},  // 编辑或新增的表单数据
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),  // 从本地存储获取当前用户信息
      rules: {  // 表单验证规则
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' },
        ],
        content: [
          { required: true, message: '请输入内容', trigger: 'blur' },
        ]
      },
      ids: []  // 选中的记录 ID 列表
    }
  },
  created() {
    this.load(1)  // 初始化加载第一页的数据
  },
  methods: {
    handleAdd() {   // 新增数据
      this.form = {}  // 清空表单数据
      this.fromVisible = true   // 显示对话框
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 深拷贝选中的行数据到表单中
      this.fromVisible = true   // 显示对话框
    },
    save() {   // 保存数据
      this.$refs.formRef.validate((valid) => {  // 验证表单
        if (valid) {
          this.$request({
            url: this.form.id ? '/notice/update' : '/notice/add',  // 判断是更新还是新增
            method: this.form.id ? 'PUT' : 'POST',  // 更新用 PUT，新增用 POST
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 如果保存成功
              this.$message.success('保存成功')  // 显示成功提示
              this.load(1)  // 重新加载第一页
              this.fromVisible = false  // 关闭对话框
            } else {
              this.$message.error(res.msg)  // 显示错误信息
            }
          })
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', { type: "warning" }).then(response => {
        this.$request.delete('/notice/delete/' + id).then(res => {
          if (res.code === '200') {   // 如果删除成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 显示错误信息
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {   // 更新选中的行
      this.ids = rows.map(v => v.id)  // 获取选中行的 ID
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {  // 如果没有选中任何数据
        this.$message.warning('请选择数据')  // 提示用户
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', { type: "warning" }).then(response => {
        this.$request.delete('/notice/delete/batch', { data: this.ids }).then(res => {
          if (res.code === '200') {   // 如果批量删除成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 显示错误信息
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum  // 更新当前页码
      this.$request.get('/notice/selectPage', {  // 发起分页查询请求
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          title: this.title,  // 查询条件：公告标题
        }
      }).then(res => {
        this.tableData = res.data?.list  // 更新表格数据
        this.total = res.data?.total  // 更新总记录数
      })
    },
    reset() {  // 重置查询条件
      this.title = null  // 清空查询公告标题
      this.load(1)  // 重新加载第一页
    },
    handleCurrentChange(pageNum) {  // 分页控件页码改变
      this.load(pageNum)  // 加载指定页码的数据
    },
  }
}
</script>

<style scoped>

</style>
