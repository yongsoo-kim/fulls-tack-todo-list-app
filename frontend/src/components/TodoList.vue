<template>
  <div class="container">
    <div class="alert alert-primary" role="alert" v-if="todos.length == 0">
      There is no To-Do item. Try add one!
    </div>
    <ul class="list-group input-group">
      <li class="list-group-item" v-for="(item, index) in todos" :key="item.id">
        <input
          type="checkbox"
          @change="$emit('toggle-check', index)"
          :checked="item.checked"
        />
        <span :class="item.checked ? 'done-todo' : ''">
          {{ item.title }}
        </span>
        <button class="btn btn-danger" @click="$emit('delete-todo', item.id)">
          DELETE
        </button>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  // 이것만 해도 충분하지만, 오브젝트로 들어오는 자료타입을 규정해주자.
  //   props: ["todos"],
  props: {
    todos: {
      type: Array,
      required: true,
    },
  },
};
</script>

<style></style>
