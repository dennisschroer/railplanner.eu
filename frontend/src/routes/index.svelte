<script>
    import StationSelect from "$lib/StationSelect.svelte";

    let startStation = undefined;
    let destinationStation = undefined;
    let startTime = new Date();
</script>

<form class="row g-3">
    <div class="col-12 col-md-4">
        <label class="form-label">From</label>
        <StationSelect bind:selected={startStation}/>
    </div>

    <div class="col-12 col-md-4">
        <label class="form-label">To</label>
        <StationSelect bind:selected={destinationStation}/>
    </div>

    <div class="col-12 col-md-4">
        <label class="form-label">Departure</label>
        <div>{startTime}</div>
    </div>

    <button type="submit" class="btn btn-primary">Search journey</button>
</form>