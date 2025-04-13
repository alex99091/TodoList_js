import axios from "axios";

const BASE_URL = "/api/todos";

export const fetchTodos = () => axios.get(BASE_URL);

export const addTodo = (todo) => axios.post(BASE_URL, todo);

export const updateTodo = (todo) => axios.patch(`${BASE_URL}/${todo.id}`, todo);

export const deleteTodo = (id) => axios.delete(`${BASE_URL}/${id}`);
