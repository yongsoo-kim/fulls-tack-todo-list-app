<template>
  <div class="container">
    <!-- Todo search bar part -->
    <TodoSearchBar @searchKeyInputs="onSearchKeyInputs" />

    <hr />

    <!-- Todo add form part -->
    <TodoAddForm @add-new-todo="addNewTodo" />

    <hr />

    <!-- {{ filteredTodos }} -->

    <!-- Todo list part -->
    <TodoList
      :todos="filteredTodos"
      @toggle-check="toggleCheck"
      @delete-todo="deleteTodo"
    />
  </div>
</template>

<script>
import { computed, ref } from "vue";
import TodoSearchBar from "./components/TodoSearchBar.vue";
import TodoAddForm from "./components/TodoAddForm.vue";
import TodoList from "./components/TodoList.vue";

export default {
  components: {
    TodoSearchBar,
    TodoAddForm,
    TodoList,
  },

  setup() {
    const todos = ref([]);
    const searchKeyInputs = ref("");

    const addNewTodo = (todoObject) => {
      todos.value.push(todoObject);
    };

    const deleteTodo = (index) => {
      console.log(index);
      todos.value.splice(index, 1);
    };

    const onSearchKeyInputs = (keyInputs) => {
      searchKeyInputs.value = keyInputs;
    };

    const toggleCheck = (index) => {
      console.log(index);
      todos.value[index].checked = !todos.value[index].checked;
    };

    const filteredTodos = computed(() => {
      if (searchKeyInputs.value) {
        return todos.value.filter((todo) => {
          return todo.title.includes(searchKeyInputs.value);
        });
      } else {
        return todos.value;
      }
    });

    return {
      todos,
      addNewTodo,
      toggleCheck,
      deleteTodo,
      filteredTodos,
      searchKeyInputs,
      onSearchKeyInputs,
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
