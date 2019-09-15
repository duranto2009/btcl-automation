<html>
<head>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="vis.css">
    <style type="text/css">
        #flow-graph {
            width: 100%;
            height: 90%;
            border: 1px solid lightgray;
        }
    </style>
</head>

<body>

    <div id="app">
        <select v-model="selectedModule" @change="onSelectModule">
            <option v-for="option in moduleOptions" v-bind:value="option.value">
                {{ option.text }}
            </option>
        </select>

        <select v-model="selectedFlow" @change="onSelectFlow">
            <option v-for="option in flowOptions" v-bind:value="option.value">
                {{ option.text }}
            </option>
        </select>

        <div id="flow-graph"></div>
    </div>

    <script src="vue.js"></script>
    <script src="axios.js"></script>
    <script src="vis.js"></script>
    <script src="script.js"></script>
</body>
</html>