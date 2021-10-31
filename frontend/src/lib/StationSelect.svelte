<script >
    import Select from 'svelte-select';

    const items = [
        {value: 'chocolate', label: '🍫 Chocolate', group: 'Sweet'},
        {value: 'pizza', label: '🍕 Pizza', group: 'Savory'},
        {value: 'cake', label: '🎂 Cake', group: 'Sweet'},
        {value: 'cookies', label: '🍪 Cookies', group: 'Savory'},
        {value: 'ice-cream', label: '🍦 Ice Cream', group: 'Sweet'}
    ];

    export let selected = undefined;

    function handleSelect(event) {
        selected = event.detail;
    }

    function handleClear() {
        selected = undefined;
    }
</script>


<Select id="food" {items} on:select={handleSelect} on:clear={handleClear}></Select>