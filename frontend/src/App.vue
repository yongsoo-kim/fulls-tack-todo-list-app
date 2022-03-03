<template>
  <div class="container">
    <!-- Todo search bar part -->
    <TodoSearchBar @search-key-input="onSearchKeyInputs" />

    <hr />

    <!-- Todo add form part -->
    <TodoAddForm @add-new-todo="addNewTodo" />

    <div class="alert alert-danger" role="alert" v-if="alertMessage">
      {{ alertMessage }}
    </div>

    <hr />

    <!-- {{ filteredTodos }} -->

    <!-- Todo list part -->
    <TodoList
      :todos="todos"
      @toggle-check="toggleCheck"
      @delete-todo="deleteTodo"
    />

    <!-- Todo list pagination -->
    {{ totalPageCount }}
    {{ currentPage }}
    <div class="m-5" v-if="todos.length != 0">
      <nav aria-label="Page navigation example">
        <ul class="pagination justify-content-center">
          <li class="page-item" :class="{ disabled: currentPage == 1 }">
            <a class="page-link" href="#" @click="movePage(currentPage - 1)"
              >Previous</a
            >
          </li>

          <li
            class="page-item"
            v-for="pageNo in totalPageCount"
            :key="pageNo"
            :class="{ active: pageNo == currentPage }"
          >
            <a class="page-link" href="#" @click="movePage(pageNo)">{{
              pageNo
            }}</a>
          </li>

          <li
            class="page-item"
            :class="{ disabled: currentPage == totalPageCount }"
          >
            <a class="page-link" href="#" @click="movePage(currentPage + 1)"
              >Next</a
            >
          </li>
        </ul>
      </nav>
    </div>
  </div>
</template>

<script>
import { computed, ref } from "vue";
import TodoSearchBar from "./components/TodoSearchBar.vue";
import TodoAddForm from "./components/TodoAddForm.vue";
import TodoList from "./components/TodoList.vue";
// TODO: main.js에서 전역변수로 사용할수 있지 않을까?
import axios from "axios";

export default {
  components: {
    TodoSearchBar,
    TodoAddForm,
    TodoList,
  },

  setup() {
    const todos = ref([]);
    const searchKeyInputs = ref("");
    const alertMessage = ref("");

    const totalTodoCount = ref(0);
    const currentPage = ref(1);
    const defaultPageSize = 5;

    const totalPageCount = computed(() => {
      return Math.ceil(totalTodoCount.value / 5);
    });

    const getTodos = async (pageNo = 1) => {
      alertMessage.value = "";

      currentPage.value = pageNo;

      // Todo: 더 우아하게 할수 있을까?
      pageNo = pageNo - 1;

      try {
        const res = await axios.get(
          `http://localhost:8888/todo?page=${pageNo}&size=${defaultPageSize}&title_like=${searchKeyInputs.value}`
        );
        todos.value = res.data.data;
        // Get total Todo counts
        totalTodoCount.value = res.headers["x-total-count"];
      } catch (err) {
        console.log(err);
        alertMessage.value = "Something went wrong. Cannot load todo items.";
      }
    };

    getTodos();

    const addNewTodo = async (todoObject) => {
      alertMessage.value = "";

      try {
        await axios.post("http://localhost:8888/todo", {
          checked: todoObject.checked,
          title: todoObject.title,
          contents: todoObject.contents,
        });
        // todos.value.push(res.data);

        getTodos();
      } catch (err) {
        console.log(err);
        //TODO: 에러 메시지 표시 할 방법 찾아라.
        alertMessage.value =
          "Something went wrong. Cannot add a new todo item.";
      }
    };

    const deleteTodo = async (id) => {
      alertMessage.value = "";

      try {
        await axios.delete(`http://localhost:8888/todo/${id}`);
        getTodos();
      } catch (err) {
        console.log(err);
        alertMessage.value =
          "Something went wrong. Cannot delete an todo item.";
      }
    };

    const onSearchKeyInputs = (keyInputs) => {
      searchKeyInputs.value = keyInputs;
      getTodos();
    };

    const toggleCheck = async (index) => {
      alertMessage.value = "";

      let id = todos.value[index].id;

      try {
        await axios.put(`http://localhost:8888/todo/${id}`, {
          checked: !todos.value[index].checked,
        });
      } catch (err) {
        console.log(err);
        alertMessage.value =
          "Something went wrong. Cannot delete an todo item.";
      }
      todos.value[index].checked = !todos.value[index].checked;
    };

    const movePage = (pageNo) => {
      currentPage.value = pageNo;
      getTodos(pageNo);
    };

    return {
      todos,
      addNewTodo,
      toggleCheck,
      deleteTodo,
      searchKeyInputs,
      onSearchKeyInputs,
      alertMessage,
      totalTodoCount,
      totalPageCount,
      currentPage,
      movePage,
    };
  },
};
</script>

<style>
.done-todo {
  text-decoration: line-through;
}

.has-search .form-control {
  padding-left: 2.375rem;
}

.has-search .form-control-feedback {
  position: absolute;
  z-index: 2;
  display: block;
  width: 2.375rem;
  height: 2.375rem;
  line-height: 2.375rem;
  text-align: center;
  pointer-events: none;
  color: #aaa;
}
</style>
