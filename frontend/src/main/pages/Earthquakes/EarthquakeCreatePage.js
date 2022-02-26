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

  const tstId = React.useRef(null);

  const onSuccess = () => {};

  const mutation = useBackendMutation(
    objectToAxiosParams,
    { onSuccess },
    // Stryker disable next-line all : hard to set up test for caching
    ["/api/earthquakes/all"]
  );

  const { isSuccess } = mutation;

  const onSubmit = async (data) => {
    mutation.mutate(data);
  };

  if (isSuccess) {
    if (mutation.data.length == 1) {
      tstId.current = toast(mutation.data.length + " Earthquake Retrieved", {
        toastId: tstId.current,
      });
    } else {
      tstId.current = toast(mutation.data.length + " Earthquakes Retrieved", {
        toastId: tstId.current,
      });
    }
    return <Navigate to="/earthquakes/list" />;
  }

  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>Retrieve New Earthquake Entries</h1>
        <EarthquakesForm submitAction={onSubmit} />
      </div>
    </BasicLayout>
  );
}
