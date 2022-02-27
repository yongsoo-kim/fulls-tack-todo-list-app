<template>
  <form @submit.prevent="addNewTodo">
    <div class="input-group m-3">
      <input
        type="text"
        class="form-control"
        placeholder="Please type a new thing to do..."
        v-model="newTodo"
      />
      <div class="input-group-append">
        <button class="btn btn-primary" type="button" @click="addNewTodo">
          ADD
        </button>
      </div>
    </div>
  </form>
</template>

<script>
import { ref } from "vue";

export default {
  setup(props, context) {
    const newTodo = ref("");

    const addNewTodo = () => {
      if (newTodo.value) {
        const todoObject = {
          checked: false,
          title: newTodo.value,
          contents: "new contents",
        };

        context.emit("add-new-todo", todoObject);
      }

      newTodo.value = "";
    };

    return {
      newTodo,
      addNewTodo,
    };
  },
};
</script>

<style></style>
