<template>
	<h1>{{helloworld}}</h1>
</template>
<script>
	export default {
		data() {
			return {
				helloworld : 'Test world'
			}
		},
		methods: {
			onSubmit() {
				console.log('submit!');
			}
		}
	}

</script>
