import EarthquakesForm from "main/components/Earthquakes/EarthquakesForm";
import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import { Navigate } from "react-router-dom";
import { useBackendMutation } from "main/utils/useBackend";
import { toast } from "react-toastify";
import React from "react";

export default function EarthquakeCreatePage() {
  const objectToAxiosParams = (earthquake) => ({
    url: "/api/earthquakes/retrieve",
    method: "POST",
    params: {
      distance: earthquake.dist,
      minMag: earthquake.minMag,
    },
  });

  const onSuccess = (earthquake) => {
    if (earthquake.length == 1) {
      toast(earthquake.length + " Earthquake Retrieved");
    } else {
      toast(earthquake.length + " Earthquakes Retrieved");
    }
  };

  const mutation = useBackendMutation(
    objectToAxiosParams,
    { onSuccess },
    // Stryker disable next-line all : hard to set up test for caching
    ["/api/earthquakes/all"]
  );

  const { isSuccess } = mutation;

  const onSubmit = async (dat) => {
    mutation.mutate(dat);
  };

  if (isSuccess) {
    return <Navigate to="/earthquakes/list" />;
  } else {
    return (
      <BasicLayout>
        <div className="pt-2">
          <h1>Retrieve New Earthquake Entries</h1>
          <EarthquakesForm submitAction={onSubmit} />
        </div>
      </BasicLayout>
    );
  }
}
