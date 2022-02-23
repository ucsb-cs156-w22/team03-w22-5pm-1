import React, {useState} from 'react'
import { Button, Form } from 'react-bootstrap';
import { useForm } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'


function EarthquakesForm({ earthquakesParams, retrieveAction, buttonLabel="Retrieve" }) {

    // Stryker disable all
    const {
        register,
        formState: { errors },
        handleSubmit,
    } = useForm(
        { defaultValues: earthquakesParams || {}, }
    );
    // Stryker enable all

    const navigate = useNavigate();

    // For explanation, see: https://stackoverflow.com/questions/3143070/javascript-regex-iso-datetime
    // Note that even this complex regex may still need some tweaks

    // Stryker disable next-line Regex
    // const isodate_regex = /(\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d:[0-5]\d\.\d+)|(\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d:[0-5]\d)|(\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d)/i;

    // Stryker disable next-line all
    // const yyyyq_regex = /((19)|(20))\d{2}[1-4]/i; // Accepts from 1900-2099 followed by 1-4.  Close enough.

    // Styker double Regex Pattern
    const double_regex = /[+-]?([0-9]*[.])?[0-9]+/i;


    return (

        <Form onSubmit={handleSubmit(retrieveAction)}>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="minMag">Minimum Magnitude</Form.Label>
                <Form.Control
                    data-testid="EarthquakesForm-minMag"
                    id="minMag"
                    type="number"
                    step="0.01"
                    isInvalid={Boolean(errors.minMag)}
                    {...register("minMag", { required: true })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.minMag && 'Minimum Magnitude of an earthquake is required.'}
                    {errors.minMag?.type === 'number' && 'Minimum Magnitude of an earthquake must be a number, e.g. 5.8'}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="dist">Distance</Form.Label>
                <Form.Control
                    data-testid="EarthquakesForm-dist"
                    id="dist"
                    type="number"
                    isInvalid={Boolean(errors.dist)}
                    {...register("dist", { required: true })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.dist && 'Distance in km from Storke Tower is required.'}
                    {errors.dist?.type === 'number' && 'Distance in km must be a number, e.g. 1.6km'}
                </Form.Control.Feedback>
            </Form.Group>

            <Button
                type="submit"
                data-testid="EarthquakesForm-submit"
            >
                {buttonLabel}
            </Button>
            <Button
                variant="Secondary"
                onClick={() => navigate(-1)}
                data-testid="EarthquakesForm-cancel"
            >
                Cancel
            </Button>

        </Form>

    )
}

export default EarthquakesForm;
